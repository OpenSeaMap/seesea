package net.sf.seesea.data.postprocessing.filter;

import net.sf.seesea.model.core.physx.Measurement;

public class MeasurementContainer {

	private String messageType;
	private Measurement measurement;
	public MeasurementContainer() {;}
	public MeasurementContainer( String t, Measurement m )
	{
		messageType = t;
		measurement = m;
	}
	public String MessageType() { return messageType; }
	public Measurement GetMeasurement() { return measurement; }
}
