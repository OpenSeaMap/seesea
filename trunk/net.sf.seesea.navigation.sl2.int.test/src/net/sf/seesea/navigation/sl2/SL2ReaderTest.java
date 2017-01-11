package net.sf.seesea.navigation.sl2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;

import net.sf.seesea.model.core.geo.GeoPosition3D;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.physx.Measurement;
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

	@Test
	public void testReadUTCDate() throws IOException {
		URL sl2File = FrameworkUtil.getBundle(ISL2Reader.class).findEntries("res", "timeStrippedHeader.dat", false).nextElement();
		DataInputStream dataInputStream = new DataInputStream(sl2File.openStream());
		byte[] input = new byte[4096];
		int[] input2 = new int[4096];
		int read = dataInputStream.read(input, 0, 8); // skip file header
		read = dataInputStream.read(input);
		for (int i = 0; i < input.length; i++) {
			input2[i] = input[i];
		}
		SL2Reader sl2Reader = new SL2Reader();
		final List<Measurement> measurements2 = new ArrayList<>();
		IMeasurementListener measurementListener2 = new IMeasurementListener() {
			
			@Override
			public void notify(List<Measurement> measurements) {
				measurements2.addAll(measurements);				
			}
		};
		sl2Reader.addMeasurementListener(measurementListener2);
//		int[] data = new int[144];
		sl2Reader.notifySL2Block(input2);
		MeasuredPosition3D m = (MeasuredPosition3D) measurements2.get(0);
		assertEquals(1474704286062L, m.getTime().getTime());
	}

}
