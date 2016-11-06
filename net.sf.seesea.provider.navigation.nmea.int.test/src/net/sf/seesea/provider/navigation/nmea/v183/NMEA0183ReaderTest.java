package net.sf.seesea.provider.navigation.nmea.v183;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.junit.Test;

import net.sf.seesea.model.core.geo.Depth;
import net.sf.seesea.model.core.geo.GNSSMeasuredPosition;
import net.sf.seesea.model.core.geo.RelativeDepthMeasurementPosition;
import net.sf.seesea.model.core.physx.CompositeMeasurement;
import net.sf.seesea.model.core.physx.Distance;
import net.sf.seesea.model.core.physx.DistanceType;
import net.sf.seesea.model.core.physx.HandOrientation;
import net.sf.seesea.model.core.physx.Heading;
import net.sf.seesea.model.core.physx.HeadingType;
import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.model.core.physx.RelativeSpeed;
import net.sf.seesea.model.core.physx.RelativeWind;
import net.sf.seesea.model.core.physx.Speed;
import net.sf.seesea.model.core.physx.SpeedType;
import net.sf.seesea.model.core.physx.SpeedUnit;
import net.sf.seesea.model.core.physx.Temperature;
import net.sf.seesea.model.core.physx.TemperatureUnit;
import net.sf.seesea.model.core.physx.Time;
import net.sf.seesea.model.core.weather.Reference;
import net.sf.seesea.model.core.weather.WindMeasurement;
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

	@Test
	public void testReaderMWV() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("03:46:37.560;B;$WIMWV,6.4,R,9.4,S,A*31", results);
		WindMeasurement windMeasurement = (WindMeasurement) extractMeasurementsFromNMEA.get(0);
		assertEquals(Reference.RELATIVE,windMeasurement.getReference());
		assertEquals(6.4,windMeasurement.getAngle(), 0.000001);

		assertEquals(9.4,windMeasurement.getSpeed(), 0.000001);
		assertEquals(SpeedUnit.UNKNOWN,windMeasurement.getSpeedUnit());
		assertTrue(windMeasurement.isValid());
		
	}

	@Test
	public void testReaderHDMOK() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("04:00:03.657;B;$IIHDM,194.0,M*2E", results);
		Heading heading = (Heading) extractMeasurementsFromNMEA.get(0);
		assertEquals(HeadingType.MAGNETIC,heading.getHeadingType());
		assertEquals(194.0,heading.getDegrees(), 0.000001);
		assertEquals("II",heading.getSensorID());
		assertTrue(heading.isValid());
	}


	@Test
	public void testReaderHDMWrongIdentifier() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("04:00:03.657;B;$IIHDM,194.0,T*37", results);
		Heading heading2 = (Heading) extractMeasurementsFromNMEA.get(0);
		assertEquals(HeadingType.UNKNOWN,heading2.getHeadingType());
		assertEquals(194.0,heading2.getDegrees(), 0.000001);
		assertFalse(heading2.isValid());
	}
	

	@Test
	public void testReaderHDMEmptyValue() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("04:00:03.657;B;$IIHDM,,M*0C", results);
		Heading heading2 = (Heading) extractMeasurementsFromNMEA.get(0);
		assertEquals(HeadingType.MAGNETIC,heading2.getHeadingType());
		assertEquals(0.0,heading2.getDegrees(), 0.000001);
		assertFalse(heading2.isValid());
	}

	@Test
	public void testReaderMTWOkCelcius() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("04:00:11.060;B;$IIMTW,14.9,C*1F", results);
		Temperature temperature = (Temperature) extractMeasurementsFromNMEA.get(0);
		assertEquals(TemperatureUnit.CELSIUS,temperature.getUnit());
		assertEquals(14.9,temperature.getValue(), 0.00001);
		assertEquals("II",temperature.getSensorID());
		assertTrue(temperature.isValid());
	}

	@Test
	public void testReaderMTWOkFahrenheit() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("04:00:11.060;B;$IIMTW,14.9,F*1A", results);
		Temperature temperature = (Temperature) extractMeasurementsFromNMEA.get(0);
		assertEquals(TemperatureUnit.FAHRENHEIT,temperature.getUnit());
		assertEquals(14.9,temperature.getValue(), 0.00001);
		assertEquals("II",temperature.getSensorID());
		assertTrue(temperature.isValid());
	}

	@Test
	public void testReaderVHWOk() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("04:00:10.100;B;$IIVHW,196.0,T,189.0,M,2.51,N,4.65,K*5A", results);
		Measurement h1 = extractMeasurementsFromNMEA.get(0);
		assertTrue(h1 instanceof CompositeMeasurement);
		CompositeMeasurement compositeMeasurement = (CompositeMeasurement) h1;
		List<Measurement> measurements = compositeMeasurement.getMeasurements();
		assertEquals(3, measurements.size());
		Heading heading1 = (Heading) measurements.get(0);
		assertEquals(196.0, heading1.getDegrees(), 0.000001);
		assertEquals("II", heading1.getSensorID());
		assertEquals(HeadingType.TRUE, heading1.getHeadingType());
		assertTrue(heading1.isValid());

		Heading heading2 = (Heading) measurements.get(1);
		assertEquals(189.0, heading2.getDegrees(), 0.000001);
		assertEquals("II", heading2.getSensorID());
		assertEquals(HeadingType.MAGNETIC, heading2.getHeadingType());
		assertTrue(heading2.isValid());

		RelativeSpeed relativeSpead1 = (RelativeSpeed) measurements.get(2);
		assertEquals(SpeedType.SPEEDTHOUGHWATER, relativeSpead1.getKey());
		assertEquals("II",relativeSpead1.getSensorID());
		assertNotNull(relativeSpead1.getValue());
		Speed speed = relativeSpead1.getValue();
		
		assertEquals(2.51,speed.getSpeed(), 0.000001);
		assertEquals(SpeedUnit.N,speed.getSpeedUnit());
		assertTrue(speed.isValid());

		}
	
	@Test
	public void testReaderZDAOk() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("$GPZDA,172809.456,12,07,1996,00,00*57", results);
		Time time = (Time) extractMeasurementsFromNMEA.get(0);
		assertEquals("UTC",time.getTimezone());
		assertEquals(837192489000L, time.getTime().getTime());
	}

	@Test
	public void testReaderVWROk() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("$IIVWR,084.0,R,10.4,N,5.4,M,19.3,K*4A", results);
		RelativeWind relativeWind = (RelativeWind) extractMeasurementsFromNMEA.get(0);
		assertEquals(HandOrientation.RIGHT, relativeWind.getBowOrientation());
		assertEquals(84.0, relativeWind.getDegrees(), 0.00001);
		assertEquals(SpeedUnit.N, relativeWind.getSpeedUnit());
	}

	@Test
	public void testReaderGGAOk() throws IOException {
		
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("$GPGGA,210853,3919.897,S,00310.223,W,2,07,1.5,0.0,M,,M,,*5B", results);
		GNSSMeasuredPosition position = (GNSSMeasuredPosition) extractMeasurementsFromNMEA.get(0);
		assertEquals(-39.33161666666667, position.getLatitude().getDecimalDegree(), 0.00001);
		assertEquals(-3.1703833333333336, position.getLongitude().getDecimalDegree(), 0.00001);
		String string = position.getAugmentation().get(0);
		assertTrue(string.equals("DGPS"));
		
//		assertEquals(84.0, relativeWind.getDegrees(), 0.00001);
//		assertEquals(SpeedUnit.N, relativeWind.getSpeedUnit());
	}

	@Test
	public void testReaderVLWOk() throws IOException {
		NMEA0183Reader nmea0183Reader = new NMEA0183Reader();
		List<Measurement> results = new ArrayList<Measurement>();
		List<Measurement> extractMeasurementsFromNMEA = nmea0183Reader.extractMeasurementsFromNMEA("$IIVLW,7803.2,N,0.00,N*43", results);
		Distance distance = (Distance) extractMeasurementsFromNMEA.get(0);
		assertEquals(DistanceType.TOTAL, distance.getDistanceType());
		assertEquals(7803.2, distance.getValue(), 0.00001);
	}

	

	
	//GPGGA,210853,9119.897,S,00310.223,W,2,07,1.5,0.0,M,,M,,
}

