package net.sf.seesea.data.postprocessing.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;

import net.sf.seesea.data.io.IWriterFactory;
import net.sf.seesea.data.io.MemoryDataWriter;
import net.sf.seesea.data.io.WriterException;
import net.sf.seesea.model.core.geo.Depth;
import net.sf.seesea.model.core.geo.GeoFactory;
import net.sf.seesea.model.core.geo.Latitude;
import net.sf.seesea.model.core.geo.Longitude;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.track.api.data.IBoatParameters;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.exception.ProcessingException;
import net.sf.seesea.waterlevel.IWaterLevelCorrection;
import net.sf.seesea.waterlevel.ocean.ITideProvider;

public class UnfilteredMeasurementProcessorTest {

	@Test
	public void testProcessEmptyMeasurments() throws ProcessingException {

		List<Measurement> measurements = new ArrayList<>();
		UnfilteredMeasurementProcessor unfilteredMeasurementProcessor = new UnfilteredMeasurementProcessor();
		unfilteredMeasurementProcessor.processMeasurements(measurements, "Pos", null);
		
		return;
		
	}

	@Test
	public void testProcessSingleMeasurments() throws ProcessingException, WriterException {
		List<Measurement> measurements = new ArrayList<>();
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		
		MeasuredPosition3D measuredPosition3D = geoFactory.createMeasuredPosition3D();
		Latitude latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(10);
		measuredPosition3D.setLatitude(latitude);
		Longitude longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(54.0);
		measuredPosition3D.setLongitude(longitude);
		measuredPosition3D.setValid(true);
		
		Depth depth = geoFactory.createDepth();
		depth.setValid(true);
		measurements.add(measuredPosition3D);
		measurements.add(depth);
		
		MemoryDataWriter memoryDataWriter = new MemoryDataWriter();
		
		UnfilteredMeasurementProcessor unfilteredMeasurementProcessor = new UnfilteredMeasurementProcessor();
		unfilteredMeasurementProcessor.bindWriter(memoryDataWriter);
		
		ITrackFile trackFile = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.replay(trackFile);
		
		unfilteredMeasurementProcessor.processMeasurements(measurements, "Pos", trackFile);
		unfilteredMeasurementProcessor.finish();
		
		List<Measurement> writtenMeasurements = memoryDataWriter.getMeasurements();
		assertEquals(2, writtenMeasurements.size());
		assertTrue(writtenMeasurements.contains(measuredPosition3D));
		assertTrue(writtenMeasurements.contains(depth));
		
	}
	
	@Test
	public void testBoatOffsetProcessSingleMeasurments() throws ProcessingException, WriterException {
		List<Measurement> measurements = new ArrayList<>();
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		
		MeasuredPosition3D measuredPosition3D = geoFactory.createMeasuredPosition3D();
		Latitude latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(10);
		measuredPosition3D.setLatitude(latitude);
		Longitude longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(54.0);
		measuredPosition3D.setLongitude(longitude);
		measuredPosition3D.setValid(true);
		measuredPosition3D.setSensorID("Pos");
		
		Depth depth = geoFactory.createDepth();
		depth.setValid(true);
		depth.setDepth(10.0);
		measurements.add(measuredPosition3D);
		measurements.add(depth);
		
		MemoryDataWriter memoryDataWriter = new MemoryDataWriter();
		
		UnfilteredMeasurementProcessor unfilteredMeasurementProcessor = new UnfilteredMeasurementProcessor();
		Map<String, Object> map = new HashMap<>();
		map.put("boatOffsetFiltering", "true");
		unfilteredMeasurementProcessor.activate(map);
		unfilteredMeasurementProcessor.bindWriter(memoryDataWriter);

		IBoatParameters boatParameters = EasyMock.createNiceMock(IBoatParameters.class);
		EasyMock.expect(boatParameters.getSensorOffsetToWaterline("Pos")).andReturn(0.54);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(boatParameters);
		
		ITrackFile trackFile = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFile.getBoatParameters()).andReturn(boatParameters);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackFile);
		
		unfilteredMeasurementProcessor.processMeasurements(measurements, "Pos", trackFile);
		unfilteredMeasurementProcessor.finish();
		
		List<Measurement> writtenMeasurements = memoryDataWriter.getMeasurements();
		assertEquals(2, writtenMeasurements.size());
		Depth depthResult = (Depth) writtenMeasurements.get(1);
		assertEquals(10 - 0.54,depthResult.getDepth(), 0.0000001);
		
	}
	
	@Test
	public void testBoatOffsetAndTideProcessSingleMeasurments() throws ProcessingException, WriterException {
		List<Measurement> measurements = new ArrayList<>();
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		
		MeasuredPosition3D measuredPosition3D = geoFactory.createMeasuredPosition3D();
		Latitude latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(10);
		measuredPosition3D.setLatitude(latitude);
		Longitude longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(54.0);
		measuredPosition3D.setLongitude(longitude);
		measuredPosition3D.setValid(true);
		measuredPosition3D.setSensorID("Pos");
		Date time = Calendar.getInstance().getTime();
		measuredPosition3D.setTime(time);
		
		Depth depth = geoFactory.createDepth();
		depth.setValid(true);
		depth.setDepth(10.0);
		measurements.add(measuredPosition3D);
		measurements.add(depth);
		
		MemoryDataWriter memoryDataWriter = new MemoryDataWriter();
		
		UnfilteredMeasurementProcessor unfilteredMeasurementProcessor = new UnfilteredMeasurementProcessor();
		Map<String, Object> map = new HashMap<>();
		map.put("boatOffsetFiltering", "true");
		unfilteredMeasurementProcessor.activate(map);
		unfilteredMeasurementProcessor.bindWriter(memoryDataWriter);

		IBoatParameters boatParameters = EasyMock.createNiceMock(IBoatParameters.class);
		EasyMock.expect(boatParameters.getSensorOffsetToWaterline("Pos")).andReturn(0.54);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(boatParameters);
		
		ITrackFile trackFile = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFile.getBoatParameters()).andReturn(boatParameters);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackFile);


		IWaterLevelCorrection tideProvider = EasyMock.createNiceMock(IWaterLevelCorrection.class);
		EasyMock.expect(tideProvider.getCorrection(EasyMock.anyDouble(), EasyMock.anyDouble(), EasyMock.<Date>anyObject())).andReturn(0.75);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(tideProvider);
		unfilteredMeasurementProcessor.bindWaterLevelCorrection(tideProvider);
		
		unfilteredMeasurementProcessor.processMeasurements(measurements, "Pos", trackFile);
		unfilteredMeasurementProcessor.finish();
		
		List<Measurement> writtenMeasurements = memoryDataWriter.getMeasurements();
		assertEquals(2, writtenMeasurements.size());
		Depth depthResult = (Depth) writtenMeasurements.get(1);
		assertEquals(10 - 0.54 - 0.75,depthResult.getDepth(), 0.0000001);
		
	}
	
	@Test
	public void testInvalidPosition() throws ProcessingException, WriterException {
		List<Measurement> measurements = new ArrayList<>();
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		
		MeasuredPosition3D measuredPosition3D = geoFactory.createMeasuredPosition3D();
		Latitude latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(275);
		measuredPosition3D.setLatitude(latitude);
		Longitude longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(54.0);
		measuredPosition3D.setLongitude(longitude);
		measuredPosition3D.setValid(true);
		measuredPosition3D.setSensorID("Pos");
		
		Depth depth = geoFactory.createDepth();
		depth.setValid(true);
		depth.setDepth(10.0);
		measurements.add(measuredPosition3D);
		measurements.add(depth);
		
		MemoryDataWriter memoryDataWriter = new MemoryDataWriter();
		
		UnfilteredMeasurementProcessor unfilteredMeasurementProcessor = new UnfilteredMeasurementProcessor();
		Map<String, Object> map = new HashMap<>();
		map.put("boatOffsetFiltering", "true");
		unfilteredMeasurementProcessor.activate(map);
		unfilteredMeasurementProcessor.bindWriter(memoryDataWriter);

		IBoatParameters boatParameters = EasyMock.createNiceMock(IBoatParameters.class);
		EasyMock.expect(boatParameters.getSensorOffsetToWaterline("Pos")).andReturn(0.54);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(boatParameters);
		
		ITrackFile trackFile = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFile.getBoatParameters()).andReturn(boatParameters);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackFile);
		
		unfilteredMeasurementProcessor.processMeasurements(measurements, "Pos", trackFile);
		unfilteredMeasurementProcessor.finish();
		
		List<Measurement> writtenMeasurements = memoryDataWriter.getMeasurements();
		assertEquals(0, writtenMeasurements.size());
		
	}

}
