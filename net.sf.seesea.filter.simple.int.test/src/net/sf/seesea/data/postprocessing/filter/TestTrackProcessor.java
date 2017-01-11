package net.sf.seesea.data.postprocessing.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.track.api.IMeasurmentProcessor;
import net.sf.seesea.track.api.ITrackFileProcessor;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.data.SensorDescriptionUpdateRate;
import net.sf.seesea.track.api.exception.ProcessingException;

public class TestTrackProcessor implements ITrackFileProcessor {
	
	private List<Measurement> measurements;
	private IMeasurmentProcessor measurmentProcessor;
	private boolean relativeTime;

	public TestTrackProcessor(List<Measurement> measurements, boolean relativeTime) {
		this.measurements = measurements;
		this.relativeTime = relativeTime;
	}

	@Override
	public void processFile(ITrackFile trackFile) throws IOException, ProcessingException {
		measurmentProcessor.processMeasurements(measurements, "II", trackFile);

	}

	@Override
	public void setMeasurementProcessor(IMeasurmentProcessor measurmentProcessor) {
		this.measurmentProcessor = measurmentProcessor;
	}

	@Override
	public void setFilter(Set<SensorDescriptionUpdateRate<Measurement>> bestSensors) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasRelativeTime() {
		return relativeTime;
	}

	@Override
	public boolean hasAbsoluteTime() {
		// TODO Auto-generated method stub
		return false;
	}

}
