/**
Copyright (c) 2013-2016, Jens Kübler
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package net.sf.seesea.data.postprocessing.filter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.sf.seesea.data.io.WriterException;
import net.sf.seesea.data.postprocessing.process.FilterException;
import net.sf.seesea.data.postprocessing.process.IFileTypeProcessingFactory;
import net.sf.seesea.data.postprocessing.process.IFilter;
import net.sf.seesea.data.postprocessing.process.IFilterController;
import net.sf.seesea.data.postprocessing.process.IStatisticsPreprocessor;
import net.sf.seesea.data.postprocessing.process.StatisticsException;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.track.api.ITrackFileProcessor;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.data.SensorDescriptionUpdateRate;
import net.sf.seesea.track.api.exception.ProcessingException;

/**
 * The main class that controls a filtering process for a cluster of track files. A cluster of track files
 * is a ordered
 */
@Component
public class FilterController implements IFilterController {

	private AtomicReference<IFileTypeProcessingFactory> fileTypeProcessingFactoryAR = new AtomicReference<IFileTypeProcessingFactory>();

	private List<IFilter> filters = Collections.synchronizedList(new ArrayList<IFilter>());

	/**
	 * process this list of tracks as batch
	 * 
	 * @param orderedFiles
	 *            the tracks to process at once
	 * @param executeSensorDistribution
	 *            true, if multiple sensor are available and the best one needs
	 *            to be chosen
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws WriterException
	 * @throws ProcessingException
	 */
	public void process(Collection<ITrackFile> orderedFiles, boolean executeSensorDistribution) throws FilterException {

		IFileTypeProcessingFactory processingFactory = fileTypeProcessingFactoryAR.get();
		Logger logger = Logger.getLogger(getClass());
		// when the file type changes, consider it a new track
		String lastFileType = orderedFiles.iterator().next().getFileType();
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		ITrackFileProcessor preprocessor = null;
		IStatisticsPreprocessor measurmentProcessor = new DepthPositionMeasurementStatisticsProcessor<>();
		for (ITrackFile trackFile : orderedFiles) {
			try {
				if (trackFile.getFileType() == null) {
					// hm must be a zip file
				} else {
					preprocessor = processingFactory.createLocationPreProcessor(trackFile);
					preprocessor.setMeasurementProcessor(measurmentProcessor);
					if (!trackFile.getFileType().equals(lastFileType)) {
						// file type changed -> run statistics again
						if (preprocessor != null) {
							Set<SensorDescriptionUpdateRate<Measurement>> bestSensors = measurmentProcessor.getBestSensors();
							runFilters(trackFiles, bestSensors);
						}
						trackFiles.clear();
						lastFileType = trackFile.getFileType();
						if (executeSensorDistribution) {
//							try {
								preprocessor.processFile(trackFile);
//							} catch (StatisticsException e) {
//								// must be correct at this point since the
//								// preprocessor passed it with depth points
//								logger.error("Partially correct data for for track id " + trackFile.getTrackId());
//							}
						}
						trackFiles.add(trackFile);
					} else {
						// continue processing this file
						if (executeSensorDistribution) {
							preprocessor.processFile(trackFile);
						}
						trackFiles.add(trackFile);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Failed to process file", e); //$NON-NLS-1$
			}
		}
		try {
			// trigger any open filter run
			if (!trackFiles.isEmpty() && executeSensorDistribution) {
				Set<SensorDescriptionUpdateRate<Measurement>> bestSensors = measurmentProcessor.getBestSensors();
				runFilters(trackFiles, bestSensors);
			} else if (!trackFiles.isEmpty()) {
				Set<SensorDescriptionUpdateRate<Measurement>> noSensors = new HashSet<SensorDescriptionUpdateRate<Measurement>>();
				runFilters(trackFiles, noSensors);
			}
		} catch (IOException | ProcessingException | StatisticsException e) {
			throw new FilterException(e);
		}
	}

	/**
	 * 
	 * @param orderedFiles
	 *            the list of tracks to process as batch
	 * @param bestSensors
	 *            the sensors to be used for filterung. may be an empty set if
	 *            there is only one sensor per measurement
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ProcessingException
	 */
	private void runFilters(List<ITrackFile> orderedFiles, Set<SensorDescriptionUpdateRate<Measurement>> bestSensors)
			throws IOException, ProcessingException {
		IFileTypeProcessingFactory fileTypeProcessingFactory = fileTypeProcessingFactoryAR.get();

		logBestSensorChoice(bestSensors);
		// System.out.println("Wrinting Filter data to " + format);

		boolean processFiles4Filter = false;

		ITrackFile firstTrack = orderedFiles.iterator().next();
		for (IFilter filter : filters) {
			try {
				ITrackFileProcessor trackFileProcessor = fileTypeProcessingFactory.createLocationPreProcessor(firstTrack);
				trackFileProcessor.setFilter(bestSensors);
				// tie the reading processor to the filter
				filter.setBestSensors(bestSensors);
				trackFileProcessor.setMeasurementProcessor(filter);
				if (filter.requiresAbsoluteTime() && trackFileProcessor.hasAbsoluteTime()) {
					processFiles4Filter = true;
				} else if (filter.requiresRelativeTime() && trackFileProcessor.hasRelativeTime()) {
					processFiles4Filter = true;
				} else if (!filter.requiresAbsoluteTime() && !filter.requiresRelativeTime()) {
					processFiles4Filter = true;
				}
				if (!processFiles4Filter) {
					Logger.getLogger(getClass()).info("Skipping filter run for track file processor " + trackFileProcessor
							+ " with filter " + filter);
				}
				if (processFiles4Filter) {
					System.out.println("Running filter run for track file processor " + trackFileProcessor + " with filter "
							+ filter);
					for (ITrackFile trackFile : orderedFiles) {
						try {
							System.out.println("Processing track id:" + trackFile.getTrackId());
							trackFileProcessor.processFile(trackFile);
						} catch (ProcessingException e) {
							Logger.getLogger(getClass())
							.error("Partially correct data for for track id " + trackFile.getTrackId());
						}
					}
					filter.finish();
				}
				
			} finally {
				fileTypeProcessingFactory.disposeLocationPreProcessor(firstTrack);
			}
		}
	}

	private void logBestSensorChoice(Set<SensorDescriptionUpdateRate<Measurement>> bestSensors) {
		System.out.println("Best sensors chosen for processing are");
		for (SensorDescriptionUpdateRate<Measurement> sensorDescriptionUpdateRate : bestSensors) {
			if (sensorDescriptionUpdateRate != null) {
				System.out.println(sensorDescriptionUpdateRate.toString());
			}
		}
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void bindFileTypeProcessingFactory(IFileTypeProcessingFactory fileTypeProcessingFactory) {
		fileTypeProcessingFactoryAR.set(fileTypeProcessingFactory);
	}

	public void unbindFileTypeProcessingFactory(IFileTypeProcessingFactory fileTypeProcessingFactory) {
		fileTypeProcessingFactoryAR.compareAndSet(null, fileTypeProcessingFactory);
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	public void bindFilter(IFilter filter) {
		filters.add(filter);
	}

	public void unbindFilter(IFilter filter) {
		filters.remove(filter);
	}

}
