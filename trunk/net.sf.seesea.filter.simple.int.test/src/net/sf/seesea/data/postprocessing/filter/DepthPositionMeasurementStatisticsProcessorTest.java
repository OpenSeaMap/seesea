package net.sf.seesea.data.postprocessing.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

import net.sf.seesea.model.core.geo.Depth;
import net.sf.seesea.model.core.geo.GeoFactory;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.track.api.ITrackFileProcessor;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.data.SensorDescriptionUpdateRate;

public class DepthPositionMeasurementStatisticsProcessorTest {

	@Test
	public void testLocationMeasurements() {
		ITrackFileProcessor trackFileProcessor = EasyMock.createNiceMock(ITrackFileProcessor.class);
		EasyMock.replay(trackFileProcessor);
		DepthPositionMeasurementStatisticsProcessor<Measurement> depthPositionMeasurementStatisticsProcessor = new DepthPositionMeasurementStatisticsProcessor<>();
	
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		calendar.add(Calendar.SECOND, 1);
		Date time2 = calendar.getTime();
		
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		List<Measurement> measurements = new ArrayList<>();
		MeasuredPosition3D measuredPosition3DA = geoFactory.createMeasuredPosition3D();
		MeasuredPosition3D measuredPosition3DB = geoFactory.createMeasuredPosition3D();
		measuredPosition3DA.setTime(time);
		measuredPosition3DA.setSensorID("II");
		measuredPosition3DA.setValid(true);
		measuredPosition3DB.setTime(time2);
		measuredPosition3DB.setValid(true);
		measuredPosition3DB.setSensorID("II");
		measurements.add(measuredPosition3DA);
		measurements.add(measuredPosition3DB);

		ITrackFile trackFile = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFile.isHasAbsoluteTimedMeasurements()).andReturn(true);
		EasyMock.replay(trackFile);

		depthPositionMeasurementStatisticsProcessor.processMeasurements(measurements, "GLL", trackFile);
		SensorDescriptionUpdateRate<MeasuredPosition3D> bestPositionSensor = depthPositionMeasurementStatisticsProcessor.getBestPositionSensor();
		Class<?> measurement = bestPositionSensor.getMeasurement();
		assertTrue(measurement.isAssignableFrom(MeasuredPosition3D.class));
		assertEquals(1000,bestPositionSensor.getUpdateRate());
		assertEquals("GLL",bestPositionSensor.getMessageType());
	}
	
	@Test
	@Ignore
	public void testDepthLocationMeasurements() {
		ITrackFileProcessor trackFileProcessor = EasyMock.createNiceMock(ITrackFileProcessor.class);
		EasyMock.replay(trackFileProcessor);
		DepthPositionMeasurementStatisticsProcessor<Measurement> depthPositionMeasurementStatisticsProcessor = new DepthPositionMeasurementStatisticsProcessor<>();
	
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		calendar.add(Calendar.SECOND, 1);
		Date time2 = calendar.getTime();
		
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		List<Measurement> measurements = new ArrayList<>();
		MeasuredPosition3D measuredPosition3DA = geoFactory.createMeasuredPosition3D();
		MeasuredPosition3D measuredPosition3DB = geoFactory.createMeasuredPosition3D();
		measuredPosition3DA.setTime(time);
		measuredPosition3DA.setSensorID("II");
		measuredPosition3DA.setValid(true);
		measuredPosition3DB.setTime(time2);
		measuredPosition3DB.setValid(true);
		measuredPosition3DB.setSensorID("II");
		Depth depthA = geoFactory.createDepth();
		depthA.setDepth(50);
		depthA.setSensorID("II");
		depthA.setValid(true);
		Depth depthB = geoFactory.createDepth();
		depthB.setSensorID("II");
		depthB.setDepth(50);
		depthB.setValid(true);
		measurements.add(measuredPosition3DA);
		measurements.add(depthA);
		measurements.add(measuredPosition3DB);
		measurements.add(depthB);

		ITrackFile trackFile = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFile.isHasAbsoluteTimedMeasurements()).andReturn(true);
		EasyMock.replay(trackFile);

		depthPositionMeasurementStatisticsProcessor.processMeasurements(measurements, "GLL", trackFile);
		SensorDescriptionUpdateRate<MeasuredPosition3D> bestPositionSensor = depthPositionMeasurementStatisticsProcessor.getBestPositionSensor();
		Class<?> measurement = bestPositionSensor.getMeasurement();
		assertTrue(measurement.isAssignableFrom(MeasuredPosition3D.class));
		assertEquals(1000,bestPositionSensor.getUpdateRate());
		assertEquals("GLL",bestPositionSensor.getMessageType());
		
		SensorDescriptionUpdateRate<Depth> bestDepthSensor = depthPositionMeasurementStatisticsProcessor.getBestDepthSensor();
		measurement = bestDepthSensor.getMeasurement();
		assertTrue(measurement.isAssignableFrom(Depth.class));
		assertEquals("GLL",bestDepthSensor.getMessageType());
		assertEquals(1000,bestDepthSensor.getUpdateRate());
	}
	
}
