package net.sf.seesea.navigation.sl2.test;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Test;

import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.navigation.sl2.SL2Reader;
import net.sf.seesea.track.api.IMeasurementListener;

public class SL2ReaderTest {


	@Test
	public void testReadNoData() {
		SL2Reader sl2Reader = new SL2Reader();
		IMeasurementListener measurementListener = EasyMock.createNiceMock(IMeasurementListener.class);
		Capture<List<Measurement>> measurements = Capture.<List<Measurement>>newInstance();
		measurementListener.notify(EasyMock.capture(measurements));
		sl2Reader.addMeasurementListener(measurementListener);
		int[] data = new int[144];
		sl2Reader.notifySL2Block(data);
		assertFalse(measurements.hasCaptured());
	}
	
//	public void testStreamDetection() throws IOException {
//		URL url = SL2TestActivator.getContext().getBundle().findEntries("res", "4076.dat", false).nextElement();
//		SL2StreamProcessor sl2StreamProcessor = new SL2StreamProcessor();
//		sl2StreamProcessor.addSL2Listener(new SL2Reader());
//		SL2TrackFileProcessor processor = new SL2TrackFileProcessor();
//		
////		sl2StreamProcessor.readByte(c, streamProvider);
//	}
	
}
