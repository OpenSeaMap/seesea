/**
Copyright (c) 2013-2015, Jens Kübler
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.sf.seesea.data.postprocessing.process.FilterException;
import net.sf.seesea.data.postprocessing.process.IDepthPositionPreProcessor;
import net.sf.seesea.data.postprocessing.process.IFileTypeProcessingFactory;
import net.sf.seesea.data.postprocessing.process.ITrackClustering;
import net.sf.seesea.data.postprocessing.process.TrackClusterResult;
import net.sf.seesea.track.api.ITrackFileProcessor;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.exception.ProcessingException;

/**	
 * this clustering orders files according to their recorded date.
 * This clustering currently fails if more than one track is recorded at the same time frame for a single user
 * 
 */
@Component
public class TimeBasedTrackClustering implements ITrackClustering {

	private AtomicReference<IFileTypeProcessingFactory> fileTypeProcessingFactoryAR = new AtomicReference<IFileTypeProcessingFactory>();
	
	private Logger log = Logger.getLogger(getClass());

	/**
	 * assemble tracks in order
	 * time of the track files, location of the tracks file, user name, filenames
	 * some clustering technique?
	 * output : a list of ordered tracks
	 * 
	 * @param orderedFiles
	 * @return a list of ordered tracks
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ProcessingException 
	 */
	public TrackClusterResult classifyTracks(List<ITrackFile> orderedFiles) {
		IFileTypeProcessingFactory fileTypeProcessingFactory = fileTypeProcessingFactoryAR.get();
//		System.out.println("Classifying tracks");
		List<List<ITrackFile>> clusterOfTracks = new ArrayList<List<ITrackFile>>();
		List<ITrackFile> newOrderedFiles = new ArrayList<ITrackFile>();
		Set<ITrackFile> corruptTrackFiles = new HashSet<ITrackFile>();
		Set<ITrackFile> noTrackFiles = new HashSet<ITrackFile>();
		Set<ITrackFile> noTimeMeasurementFiles = new HashSet<ITrackFile>();

		// get start and end point
		for (ITrackFile trackFile : orderedFiles) {
			ITrackFileProcessor locationPreprocessor = fileTypeProcessingFactory.createLocationPreProcessor(trackFile);
			if(locationPreprocessor != null) {
				IDepthPositionPreProcessor measurmentProcessor = new DepthPositionPreprocessor(locationPreprocessor.hasAbsoluteTime(), locationPreprocessor.hasRelativeTime());
				locationPreprocessor.setMeasurementProcessor(measurmentProcessor);
				// format supported
				try {
					try {
						locationPreprocessor.processFile(trackFile);
						trackFile.setBoundingBox(measurmentProcessor.getBoundingBox());
						if(!measurmentProcessor.hasDepthData() || measurmentProcessor.getPointCount() == 0) {
							// set no depth
							noTrackFiles.add(trackFile);
							if(log.isDebugEnabled()) {
								log.debug("Track file contains no depth data and no points: " + trackFile.getTrackId() + ":" + ":" + trackFile.getFileType());
							}
						} else {
							subclassifyTracks(newOrderedFiles, noTimeMeasurementFiles, trackFile, measurmentProcessor, locationPreprocessor);
						}
					} catch (ProcessingException e) {
						if(measurmentProcessor.getPointCount() > 0) {
							if(!measurmentProcessor.hasDepthData()) {
								// set no depth
								noTrackFiles.add(trackFile);
							} else {
								subclassifyTracks(newOrderedFiles, noTimeMeasurementFiles, trackFile, measurmentProcessor, locationPreprocessor);
							}
							Logger.getLogger(getClass()).error("Partially correct data for for track id " + trackFile.getTrackId(), e);
						} else {
							corruptTrackFiles.add(trackFile);
						}
					}
				} catch (IOException e) {
					corruptTrackFiles.add(trackFile);
					Logger.getLogger(getClass()).error("Failed to read file with track id " + trackFile.getTrackId(), e);
				}
				fileTypeProcessingFactory.disposeLocationPreProcessor(trackFile);
			}
		}
		
		Set<ITrackFile> duplicateTrackFiles = new HashSet<ITrackFile>();
		for (ITrackFile trackFileA : newOrderedFiles) {
			for (ITrackFile trackFileB : newOrderedFiles) {
				if(!trackFileA.equals(trackFileB)) {
					boolean compareTime = (trackFileA.isHasRelativeTimedMeasurements() && trackFileB.isHasRelativeTimedMeasurements()) || (trackFileA.isHasAbsoluteTimedMeasurements() && trackFileB.isHasAbsoluteTimedMeasurements()); 
					if((!compareTime || (trackFileA.getStartTime() != null && trackFileB.getStartTime() != null && trackFileA.getStartTime().equals(trackFileB.getStartTime()))) 
							&& trackFileA.getStart().getLatitude().getDecimalDegree() == trackFileB.getStart().getLatitude().getDecimalDegree()
							&& trackFileA.getStart().getLongitude().getDecimalDegree() == trackFileB.getStart().getLongitude().getDecimalDegree() 
							&& !duplicateTrackFiles.contains(trackFileA)) {
						duplicateTrackFiles.add(trackFileB);
						if(log.isDebugEnabled()) {
							log.debug("Track file " + trackFileB.getTrackId() + " is a a duplicate of track file " + trackFileA.getTrackId() + " Dropping second file");
						}
					}
				}
			}
		}
		newOrderedFiles.removeAll(duplicateTrackFiles);
		try {
			Collections.sort(newOrderedFiles, new TrackFileComparator());
		} catch (IllegalArgumentException e) {
			Logger.getLogger(getClass()).error("Failed to sort track collection"); //$NON-NLS-1$
		}
		if(log.isDebugEnabled()) {
			log.debug("Track file order for cluster is");
			for (ITrackFile trackFile : newOrderedFiles) {
				log.debug(trackFile.getTrackId() + ":" + trackFile.getStart() + ":" + trackFile.getEnd());
			}
		}
		
		List<ITrackFile> currentCluster = new ArrayList<ITrackFile>();
		clusterOfTracks.add(currentCluster);
		Iterator<ITrackFile> iterator = newOrderedFiles.iterator();
		if(iterator.hasNext()) {
			ITrackFile lastTrackFile = (ITrackFile) iterator
					.next();
			currentCluster.add(lastTrackFile);
			for (; iterator.hasNext();) {
				ITrackFile currentTrackFile = (ITrackFile) iterator.next();
				if((currentTrackFile.getStartTime() == null || lastTrackFile.getEnd() == null) || currentTrackFile.getStartTime() == null ||  lastTrackFile.getEnd().getTime() == null ||  currentTrackFile.getStartTime().getTime() - lastTrackFile.getEnd().getTime().getTime() > 20000) {
					currentCluster = new ArrayList<ITrackFile>();
					currentCluster.add(currentTrackFile);
					clusterOfTracks.add(currentCluster);
				} else {
					currentCluster.add(currentTrackFile);
				}
				lastTrackFile = currentTrackFile;
			} 
		}
		if(log.isDebugEnabled()) {
			log.debug("Track Clusters are " + clusterOfTracks.size());
			for(List<ITrackFile> cluster : clusterOfTracks) {
				log.debug("Cluster:");
				for (ITrackFile trackFile : cluster) {
					log.debug(trackFile.getTrackId() + ":" + trackFile.getStart() + ":" + trackFile.getEnd());
				}
			}
		}

		
		return new TrackClusterResult(clusterOfTracks, duplicateTrackFiles, corruptTrackFiles, noTrackFiles, noTimeMeasurementFiles);
	}

	private void subclassifyTracks(List<ITrackFile> newOrderedFiles, Set<ITrackFile> noTimeMeasurementFiles,
			ITrackFile trackFile, IDepthPositionPreProcessor locationPreprocessor, ITrackFileProcessor trackFileProcessor) {
		if(locationPreprocessor.getStart() != null && (trackFile.isHasAbsoluteTimedMeasurements() || trackFile.isHasRelativeTimedMeasurements())) {
			trackFile.setFirstPositions(locationPreprocessor.getFirstTrackPoints());
			trackFile.setStart(locationPreprocessor.getStart());
			trackFile.setEnd(locationPreprocessor.getEnd());
			newOrderedFiles.add(trackFile);
			if(log.isDebugEnabled()) {
				log.debug("Track file either absolute or relative time measurements: " + trackFile.getTrackId() + ":" + trackFile.getFileType());
			}
		} else {
			noTimeMeasurementFiles.add(trackFile);
			if(log.isDebugEnabled()) {
				log.debug("Track file contains no time measurements: " + trackFile.getTrackId() + ":" + trackFile.getFileType());
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


}
