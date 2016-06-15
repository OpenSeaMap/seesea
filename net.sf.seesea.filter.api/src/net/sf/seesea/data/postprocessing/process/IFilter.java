package net.sf.seesea.data.postprocessing.process;

import java.util.Set;

import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.track.api.IMeasurmentProcessor;
import net.sf.seesea.track.api.data.SensorDescriptionUpdateRate;

/**
 * A Filter is a measurement processor that receives measurements via
 * {@link IMeasurmentProcessor#processMeasurements(java.util.List, String, net.sf.seesea.track.api.data.ITrackFile)}
 * .</br>
 * After the last measurement {@link IMeasurmentProcessor#finish()} must be
 * called. This call MUST clear any internal state so that the filter may be reused.
 * It is up to the implementation to persist the filtered data.
 *  
 * @author Jens
 *
 */
public interface IFilter extends IMeasurmentProcessor {

	boolean requiresAbsoluteTime();

	boolean requiresRelativeTime();

	/**
	 * sets the best sensors. Quality information and sensor update rates may be determined from .
	 * @param updateRate the best sensors found
	 */
	void setBestSensors(Set<SensorDescriptionUpdateRate<Measurement>> bestSensors);

}
