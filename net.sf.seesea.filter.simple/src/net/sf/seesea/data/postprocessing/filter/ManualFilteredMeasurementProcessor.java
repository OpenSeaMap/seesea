package net.sf.seesea.data.postprocessing.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.sf.seesea.data.io.IDataWriter;
import net.sf.seesea.data.io.IWriterFactory;
import net.sf.seesea.data.io.WriterException;
import net.sf.seesea.data.postprocessing.process.IFilter;
import net.sf.seesea.model.core.geo.Depth;
import net.sf.seesea.model.core.geo.GeoPosition;
import net.sf.seesea.model.core.geo.GeoPosition3D;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.physx.CompositeMeasurement;
import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.model.util.GeoUtil;
import net.sf.seesea.track.api.data.IBoatParameters;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.data.SensorDescriptionUpdateRate;
import net.sf.seesea.track.api.exception.ProcessingException;
import net.sf.seesea.waterlevel.IWaterLevelCorrection;
import net.sf.seesea.waterlevel.WaterLevelCorrectionException;

public class ManualFilteredMeasurementProcessor implements IFilter {

//	private final Map<String, Object> outputOptions;

	private IDataWriter dataWriter = null;

	private MeasurmentWindow measurementWindow2;

	private long lastSourceTrackIdentifier;

	private final IBoatParameters boatParameters;

	private double sensorOffsetToWaterline;

//	private GeoBoundingBox boundingBox;
	
	private MeasuredPosition3D lastPosition;

//	private Object positionInvalid;

//	private boolean positionValid;

	private List<Depth> lastDepths;

	private DescriptiveStatistics descStats;

//	private DecimalFormat decimalFormat;
//
//	private List<Double> diffs;

	private AtomicReference<IWriterFactory> writerFactoryAR = new AtomicReference<IWriterFactory>();

	private AtomicReference<IWaterLevelCorrection> waterLevelCorrectionAR = new AtomicReference<IWaterLevelCorrection>();

	public ManualFilteredMeasurementProcessor(IBoatParameters boatParameters) {
//		this.writerFactory = writerFactory;
//		this.outputOptions = outputOptions;
		this.boatParameters = boatParameters;
		lastDepths = new ArrayList<Depth>();
//		decimalFormat = new DecimalFormat("###.##");
//		diffs = new ArrayList<Double>();
	}


	@Override
	public void processMeasurements(List<Measurement> results,
			String messageType,ITrackFile trackfile) throws ProcessingException {
		try {
			for (Measurement measurement : results) {
				if(measurement instanceof CompositeMeasurement) {
					CompositeMeasurement compositeMeasurement = (CompositeMeasurement) measurement;
					for (Measurement containedMeasurement : compositeMeasurement.getMeasurements()) {
						processSingleMeasurement(containedMeasurement, trackfile);
					}
				} else {
					processSingleMeasurement(measurement, trackfile);
				}
			}
		} catch (WriterException e) {
			throw new ProcessingException(e);
		}

	}
	
	protected void processSingleMeasurement(Measurement measurement, ITrackFile trackfile) throws WriterException, ProcessingException {
		if(lastSourceTrackIdentifier != trackfile.getTrackId()) {
			sensorOffsetToWaterline = boatParameters.getSensorOffsetToWaterline(measurement.getSensorID());
//			this.boundingBox = boundingBox;
			lastPosition = null;
			descStats = new DescriptiveStatistics(100);
//			diffs = new ArrayList<Double>();
		}
		this.lastSourceTrackIdentifier = trackfile.getTrackId();
		if(measurement.isValid()) {
				if(measurement instanceof MeasuredPosition3D) {
					MeasuredPosition3D position3d = (MeasuredPosition3D) measurement;
					if(position3d.getLatitude().getDecimalDegree() >= -90.0 && position3d.getLatitude().getDecimalDegree() <= 90.0 && position3d.getLongitude().getDecimalDegree() >= -180 && position3d.getLongitude().getDecimalDegree() <= 180) {
						// if measurements are too far apart, create a new writer, an kalman smoother with an initial value
						// new time measurement available: is it after the current window, create a new one
						if(measurementWindow2 != null) {
							if(lastPosition != null) {
								double distance = GeoUtil.getDistance(lastPosition, (GeoPosition) measurement);
								if(measurement.getTime() != null) {
									double travelledTimed = (measurement.getTime().getTime() - lastPosition.getTime().getTime()) / 3600000D;
									lastPosition = (MeasuredPosition3D) measurement;
									if(travelledTimed != 0) {
										double speed = distance / travelledTimed;
										if(!(speed >= 1 && speed < 8)) {
//									positionValid = false;
//									System.out.println(false);
											return;
										}
										
									}
								}
							}
							// update rates match -> process
							filterMeasurementWindow();
							measurementWindow2 = new MeasurmentWindow();
							if(measurement.getTime() != null) {
								lastPosition = (MeasuredPosition3D) measurement;
							}
//							positionValid = true;
							
						} else if(measurementWindow2 == null) {
							finish();
							createNewDataWriter();
							// create a new window and add time measurment
							measurementWindow2 = new MeasurmentWindow();
//						positionValid = true;
						}
					} else {
						return ; // discard value as it is out of bounds
					}
				}
		}
		if(measurementWindow2 != null) {
			if(measurement instanceof Depth) {
				Depth depth = (Depth) measurement;
				// sliding window depth measurment
				
//				if(lastDepths.size() > 0) {
//					double diff = (lastDepths.get(0).getDepth() - depth.getDepth());
//					descStats.addValue(diff);
//					diffs.add(diff);
//					Double outlier = getOutlier(diffs, 0.95);
//					System.out.println("Depth " + depth.getDepth() +  " change:" + decimalFormat.format(diff) +  "Outlier:" + outlier + " Stats " + descStats.getMean() + ":" + descStats.getVariance()); // + ": %:" + Math.log(lastDepths.get(0).getDepth()) / diff + " log diff:" );
//					
//				}
				
//				if(lastDepths.size() > 5) {
//					double averageDepth = 0;
//					for (Depth depthX : lastDepths) {
//						averageDepth += depthX.getDepth();
//					}
//					averageDepth = averageDepth / 5;
//					// 10 % change with respect to the water depth
//					if(Math.abs(averageDepth - depth.getDepth()) < Math.abs(averageDepth * 1.1) || ((averageDepth < 3.0) && Math.abs(averageDepth - depth.getDepth()) < 1)) {
//						lastDepths.remove(0);
//						lastDepths.add(depth);
//					}
//				} else {
//					lastDepths.add(depth);
//				}
				if(lastDepths.size() > 20) {
					lastDepths.remove(0);
					lastDepths.add(depth);
//					diffs.remove(0);
//					diffs.add(lastDepths.get(lastDepths.size() - 1).getDepth() - depth.getDepth());
				} else {
					lastDepths.add(depth);
//					diffs.add(lastDepths.get(lastDepths.size() - 1).getDepth() - depth.getDepth());
				}
				measurementWindow2.setMeasurement(measurement);
			} else {
				measurementWindow2.setMeasurement(measurement);
			}
		}
	}

	@Override
	public void finish() throws ProcessingException {
		try {
			if(measurementWindow2 != null && measurementWindow2.getPositions().size() > 0) {
				filterMeasurementWindow();
			}
			internalFinishProcessing();
		} catch (WriterException e) {
			throw new ProcessingException(e);
		}

	}
	
	private void internalFinishProcessing() throws WriterException {
		
		if(dataWriter != null) {
			dataWriter.closeOutput();
		}
		dataWriter = null;
	}

	
	private void createNewDataWriter() throws WriterException {
		dataWriter = writerFactoryAR.get().createWriter();
	}

	/**
	 * depending on the gathered data the filtering may be changed
	 * @throws IOException 
	 * @throws WriterException 
	 */
	private void filterMeasurementWindow() throws WriterException {
		if(measurementWindow2 != null) {
				if(dataWriter == null) {
					createNewDataWriter();
				}
			}
			List<GeoPosition3D> positions = measurementWindow2.getPositions();
			
			Depth depth = null;
			if(!measurementWindow2.getDepths().isEmpty() && !positions.isEmpty()) {
				MeasuredPosition3D lastPosition = (MeasuredPosition3D) positions.get(positions.size() - 1);
				List<Measurement> measurements = new ArrayList<Measurement>(2);
				measurements.add(lastPosition);
				depth = measurementWindow2.getDepths().get(measurementWindow2.getDepths().size() -1 );
				IWaterLevelCorrection tideProvider = waterLevelCorrectionAR.get();
				if(tideProvider != null) {
					double tideHeight;
					try {
						tideHeight = tideProvider.getCorrection(lastPosition.getLatitude().getDecimalDegree(), lastPosition.getLongitude().getDecimalDegree(), lastPosition.getTime());
						if(!Double.isNaN(tideHeight)) {
							depth.setDepth(depth.getDepth() - tideHeight);
							depth.setDepth(depth.getDepth() - sensorOffsetToWaterline);
							measurements.add(depth);
							dataWriter.write(measurements, true, lastSourceTrackIdentifier);
						} else {
							Logger.getLogger(getClass()).info("No water level correction for:" + lastPosition.getLatitude().getDecimalDegree() + ":" + lastPosition.getLongitude().getDecimalDegree() + ":" + lastPosition.getTime());
						}
					} catch (WaterLevelCorrectionException e) {
						Logger.getLogger(getClass()).info("No water level correction for:" + lastPosition.getLatitude().getDecimalDegree() + ":" + lastPosition.getLongitude().getDecimalDegree() + ":" + lastPosition.getTime());
					}
				} else {
					measurements.add(depth);
					dataWriter.write(measurements, true, lastSourceTrackIdentifier);
				}
			}
	}
	
	public Double getOutlier(List<Double> values, double significanceLevel) {
		AtomicReference<Double> outlier = new AtomicReference<Double>();
		double grubbs = getGrubbsTestStatistic(values, outlier);
		double size = values.size();
		if(size < 3) {
			return null;
		}
		TDistribution t = new TDistribution(size - 2.0);
		double criticalValue = t.inverseCumulativeProbability((1.0 - significanceLevel) / (2.0 * size));
		double criticalValueSquare = criticalValue * criticalValue;
		double grubbsCompareValue = ((size - 1) / Math.sqrt(size)) * 
				Math.sqrt((criticalValueSquare) / (size - 2.0 + criticalValueSquare));
//		System.out.println("critical value: " + grubbs + " - " + grubbsCompareValue);
		if(grubbs > grubbsCompareValue) {
			return outlier.get();
		} else {
			return null;
		}
	}
	
	public double getGrubbsTestStatistic(List<Double> values, AtomicReference<Double> outlier) {
		DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
		for (Double v : values) {
			descriptiveStatistics.addValue(v);
		}
		double mean = descStats.getMean();
		double stddev = descStats.getStandardDeviation();
		double maxDev = 0;
		for(Double d : values) {
			if(Math.abs(mean - d) > maxDev) {
				maxDev = Math.abs(mean - d);
				outlier.set(d);
			}
		}
		double grubbs = maxDev / stddev;
//		System.out.println("mean/stddev/maxDev/grubbs: " + mean + " - " + stddev + " - " + maxDev + " - " + grubbs);
		return grubbs;
	}
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	public void bindWriterFactory(IWriterFactory writerFactory) {
		writerFactoryAR.set(writerFactory);
	}

	public void unbindWriterFactory(IWriterFactory writerFactory) {
		writerFactoryAR.compareAndSet(writerFactory, null);
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	public void bindWaterLevelCorrection(IWaterLevelCorrection waterLevelCorrection) {
		waterLevelCorrectionAR.set(waterLevelCorrection);
	}

	public void unbindWaterLevelCorrection(IWaterLevelCorrection waterLevelCorrection) {
		waterLevelCorrectionAR.compareAndSet(waterLevelCorrection, null);
	}


	@Override
	public boolean requiresAbsoluteTime() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean requiresRelativeTime() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void setBestSensors(Set<SensorDescriptionUpdateRate<Measurement>> bestSensors) {
		// determine best setup for track files that are no more than x seconds
		// apart
//		long updateRate = 1000;
//		int positionPrecision = 0;
//
//		if (!bestSensors.isEmpty()) {
//			for (SensorDescriptionUpdateRate<Measurement> sensor : bestSensors) {
//				if (MeasuredPosition3D.class.isAssignableFrom(sensor.getMeasurement())) {
//					updateRate = sensor.getUpdateRate();
//					positionPrecision = sensor.getPrecision();
//					break;
//				}
//			}
//		}
	}

}
