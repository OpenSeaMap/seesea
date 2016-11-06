package net.sf.seesea.provider.navigation.nmea.v183;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;

import net.sf.seesea.model.core.geo.Depth;
import net.sf.seesea.model.core.geo.GNSSMeasuredPosition;
import net.sf.seesea.model.core.geo.RelativeDepthMeasurementPosition;
import net.sf.seesea.model.core.physx.CompositeMeasurement;
import net.sf.seesea.model.core.physx.Heading;
import net.sf.seesea.model.core.physx.HeadingType;
import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.model.core.physx.RelativeSpeed;
import net.sf.seesea.model.core.physx.Speed;
import net.sf.seesea.model.core.physx.SpeedType;
import net.sf.seesea.model.core.physx.SpeedUnit;
import net.sf.seesea.model.core.physx.Time;
import net.sf.seesea.provider.navigation.nmea.NMEA0183Activator;

public class NMEA0183ReaderTest {

	@Test
	public void testReaderContent() throws IOException {
		URL fileEntry = NMEA0183Activator.getContext().getBundle().findEntries("res", "9220.dat", false).nextElement();
		String file = FileLocator.resolve(fileEntry).getFile();
		
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader(new FileInputStream(file));
		Collection<Measurement> measurements = nmea0183Reader.read();
		Iterator<Measurement> iterator = measurements.iterator();
		Measurement m1 = iterator.next();
		assertTrue(m1 instanceof Depth);
		Depth depth = (Depth) m1;
		assertEquals(3.85, depth.getDepth(), 0.00000001);
		assertEquals("SD", depth.getSensorID());
		assertEquals(RelativeDepthMeasurementPosition.TRANSDUCER, depth.getMeasurementPosition());
		assertEquals(true, depth.isValid());
		assertNull(depth.getTime());
		assertNull(depth.getTimezone());
		
		Measurement m2 = iterator.next();
		assertTrue(m2 instanceof CompositeMeasurement);
		CompositeMeasurement compositeMeasurement = (CompositeMeasurement) m2;
		List<Measurement> measurements2 = compositeMeasurement.getMeasurements();
		assertEquals(4, measurements2.size());
		
		Measurement m3 = measurements2.get(0);
		GNSSMeasuredPosition gnssMeasuredPosition = (GNSSMeasuredPosition) m3;

		Measurement m4 = measurements2.get(1);
		RelativeSpeed relativeSpeed = (RelativeSpeed) m4;

		Measurement m5 = measurements2.get(2);
		Heading heading = (Heading) m5;

		Measurement m6 = measurements2.get(3);
		Time time = (Time) m6;

		assertEquals(6228, measurements.size());
	}

	@Test
	public void testReaderVTG() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("$GPVTG,32.2,T,31.7,M,4.0,N,7.5,K,D*26", results);
		assertEquals(1,extractMeasurementsFromNMEA.size());
		Measurement h1 = extractMeasurementsFromNMEA.get(0);
		assertTrue(h1 instanceof CompositeMeasurement);
		CompositeMeasurement compositeMeasurement = (CompositeMeasurement) h1;
		List<Measurement> measurements = compositeMeasurement.getMeasurements();
		assertEquals(3, measurements.size());
		Heading heading1 = (Heading) measurements.get(0);
		assertEquals(32.2, heading1.getDegrees(), 0.000001);
		assertEquals("GP", heading1.getSensorID());
		assertEquals(HeadingType.TRUE, heading1.getHeadingType());
		assertTrue(heading1.isValid());
		
		Heading heading2 = (Heading) measurements.get(1);
		assertEquals(31.7, heading2.getDegrees(), 0.000001);
		assertEquals("GP", heading2.getSensorID());
		assertEquals(HeadingType.MAGNETIC, heading2.getHeadingType());
		assertTrue(heading2.isValid());
		
		RelativeSpeed relativeSpead1 = (RelativeSpeed) measurements.get(2);
		assertEquals(SpeedType.COG, relativeSpead1.getKey());
		assertEquals("GP",relativeSpead1.getSensorID());
		assertNotNull(relativeSpead1.getValue());
		Speed speed = relativeSpead1.getValue();
		
		assertEquals(4.0,speed.getSpeed(), 0.000001);
		assertEquals(SpeedUnit.N,speed.getSpeedUnit());
		assertTrue(speed.isValid());

		return;
	}
	
	@Test
	public void testReaderDPT() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("03:46:31.972;B;$SDDPT,34.53,0.30*65", results);
		CompositeMeasurement compositeMeasurement = (CompositeMeasurement) extractMeasurementsFromNMEA.get(0);
		List<Measurement> measurements = compositeMeasurement.getMeasurements();
		assertEquals(2, measurements.size());
		Depth depth1 = (Depth) measurements.get(0);
		assertEquals(RelativeDepthMeasurementPosition.TRANSDUCER,depth1.getMeasurementPosition());
		assertEquals(34.53,depth1.getDepth(), 0.000001);

		Depth depth2 = (Depth) measurements.get(1);
		assertEquals(RelativeDepthMeasurementPosition.SURFACE,depth2.getMeasurementPosition());
		assertEquals(34.83,depth2.getDepth(), 0.000001);
		
	}
	
	
	
	
}
