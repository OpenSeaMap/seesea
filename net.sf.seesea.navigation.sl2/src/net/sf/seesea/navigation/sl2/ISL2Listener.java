package net.sf.seesea.navigation.sl2;

import net.sf.seesea.track.api.exception.RawDataEventException;

public interface ISL2Listener {

	void notifySL2Block(int[] data) throws RawDataEventException;

}
