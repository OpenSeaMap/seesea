package net.sf.seesea.data.postprocessing.process;

import java.util.Collection;

import net.sf.seesea.track.api.data.ITrackFile;

/**
 * A filter controller is responsible for executing a single filter run.
 * Implementations may store processing results in a database and must update the track file's processing states
 * @author jens
 *
 */
public interface IFilterController {

	
	/**
	 * Processes this collection of track files.
	 * 
	 * @param trackList
	 * @param executeSensorDistribution
	 * @throws FilterException
	 */
	void process(Collection<ITrackFile> trackList, boolean executeSensorDistribution) throws FilterException;

}
