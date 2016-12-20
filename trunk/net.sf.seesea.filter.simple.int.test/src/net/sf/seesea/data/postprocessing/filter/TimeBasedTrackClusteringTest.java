package net.sf.seesea.data.postprocessing.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import net.sf.seesea.data.postprocessing.process.FilterException;
import net.sf.seesea.data.postprocessing.process.IFileTypeProcessingFactory;
import net.sf.seesea.data.postprocessing.process.TrackClusterResult;
import net.sf.seesea.model.core.geo.Depth;
import net.sf.seesea.model.core.geo.GeoBoundingBox;
import net.sf.seesea.model.core.geo.GeoFactory;
import net.sf.seesea.model.core.geo.Latitude;
import net.sf.seesea.model.core.geo.Longitude;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.track.api.ITrackFileProcessor;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.model.SimpleTrackFile;

public class TimeBasedTrackClusteringTest {

	/**
	 * tests that data contributing no trackpoints is being ignored
	 * @throws FilterException
	 */
	@Test
	public void testNoDepthDataClassify() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFile = new SimpleTrackFile();
		trackFiles.add(simpleTrackFile);

		List<Measurement> measurements = new ArrayList<>();
//		Depth depth = createDepth(10);
		TestTrackProcessor testTrackProcessor = new TestTrackProcessor(measurements, true );
//		measurements.add(depth);
		
		IFileTypeProcessingFactory fileTypeProcessingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(testTrackProcessor);
		EasyMock.replay(fileTypeProcessingFactory);
		
		TimeBasedTrackClustering timeBasedTrackClustering = new TimeBasedTrackClustering();
		timeBasedTrackClustering.bindFileTypeProcessingFactory(fileTypeProcessingFactory);
		
		TrackClusterResult classifyTracks = timeBasedTrackClustering.classifyTracks(trackFiles);
		assertFalse(classifyTracks.getNodataTrackFiles().isEmpty());
		
		
	}

	
	/**
	 * tests that data having no time information is classified correctly
	 * 
	 * @throws FilterException
	 */
	@Test
	public void testNoTimeDepthDataClassify() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFile = new SimpleTrackFile();
		trackFiles.add(simpleTrackFile);
		
		GeoBoundingBox geoBoundingBox = EasyMock.mock(GeoBoundingBox.class);
		EasyMock.expect(geoBoundingBox.getTop()).andReturn(45.0);
		EasyMock.expect(geoBoundingBox.getBottom()).andReturn(30.0);
		EasyMock.expect(geoBoundingBox.getLeft()).andReturn(10.0);
		EasyMock.expect(geoBoundingBox.getRight()).andReturn(20.0);

		
		List<Measurement> measurements = new ArrayList<>();
		for(int i=0;i<200;i++) {
			final MeasuredPosition3D measuredPosition3DA = createPosition();
			measurements.add(measuredPosition3DA);
			measurements.add(createDepth(5.0));
		}

		ITrackFileProcessor locationPreProcessor = new TestTrackProcessor(measurements,true);

		IFileTypeProcessingFactory fileTypeProcessingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(locationPreProcessor);
		EasyMock.replay(fileTypeProcessingFactory);
		
		TimeBasedTrackClustering timeBasedTrackClustering = new TimeBasedTrackClustering();
		timeBasedTrackClustering.bindFileTypeProcessingFactory(fileTypeProcessingFactory);
		
		TrackClusterResult classifyTracks = timeBasedTrackClustering.classifyTracks(trackFiles);
		assertFalse(classifyTracks.getNoTimeMeasurementFiles().isEmpty());
		
		
	}


	private MeasuredPosition3D createPosition() {
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		Latitude latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.1);
		Longitude longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.1);
		final MeasuredPosition3D measuredPosition3DA = geoFactory.createMeasuredPosition3D();
		measuredPosition3DA.setTime(time);
		measuredPosition3DA.setLatitude(latitude);
		measuredPosition3DA.setLongitude(longitude);
		measuredPosition3DA.setValid(true);
		return measuredPosition3DA;
	}
	
	/**
	 * tests that data having relative time information is classified correctly
	 * 
	 * @throws FilterException
	 */
	@Test
//	@Ignore
	public void testTimeDepthDataClassifyRelative() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFile = new SimpleTrackFile();
		simpleTrackFile.setRelativeTimeMeasurements(true);
		trackFiles.add(simpleTrackFile);
		
		GeoBoundingBox geoBoundingBox = EasyMock.mock(GeoBoundingBox.class);
		EasyMock.expect(geoBoundingBox.getTop()).andReturn(45.0);
		EasyMock.expect(geoBoundingBox.getBottom()).andReturn(30.0);
		EasyMock.expect(geoBoundingBox.getLeft()).andReturn(10.0);
		EasyMock.expect(geoBoundingBox.getRight()).andReturn(20.0);
		
		List<Measurement> measurements = new ArrayList<>();
		for(int i=0;i<200;i++) {
			final MeasuredPosition3D measuredPosition3DA = createPosition();
			measurements.add(measuredPosition3DA);
			measurements.add(createDepth(5.0));
		}
		ITrackFileProcessor locationPreProcessor = new TestTrackProcessor(measurements,true);

		
		IFileTypeProcessingFactory fileTypeProcessingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(locationPreProcessor);
		EasyMock.replay(fileTypeProcessingFactory);
		
		TimeBasedTrackClustering timeBasedTrackClustering = new TimeBasedTrackClustering();
		timeBasedTrackClustering.bindFileTypeProcessingFactory(fileTypeProcessingFactory);
		
		TrackClusterResult classifyTracks = timeBasedTrackClustering.classifyTracks(trackFiles);
		assertTrue(classifyTracks.getCorruptTrackFiles().isEmpty());
		assertTrue(classifyTracks.getDuplicateTrackFiles().isEmpty());
		assertTrue(classifyTracks.getNodataTrackFiles().isEmpty());
		assertTrue(classifyTracks.getNoTimeMeasurementFiles().isEmpty());
		assertEquals(1, classifyTracks.getOrderedTrackFiles().size());
	}
	
	/**
	 * tests that data having absolute time information is classified correctly
	 * 
	 * @throws FilterException
	 */
	@Test
//	@Ignore
	public void testTimeDepthDataClassifyAbsolute() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFile = new SimpleTrackFile();
		simpleTrackFile.setAbsoluteTimeMeasurements(true);
		trackFiles.add(simpleTrackFile);
		
		GeoBoundingBox geoBoundingBox = EasyMock.mock(GeoBoundingBox.class);
		EasyMock.expect(geoBoundingBox.getTop()).andReturn(45.0);
		EasyMock.expect(geoBoundingBox.getBottom()).andReturn(30.0);
		EasyMock.expect(geoBoundingBox.getLeft()).andReturn(10.0);
		EasyMock.expect(geoBoundingBox.getRight()).andReturn(20.0);
		
		List<Measurement> measurements = new ArrayList<>();
		for(int i=0;i<200;i++) {
			final MeasuredPosition3D measuredPosition3DA = createPosition();
			measurements.add(measuredPosition3DA);
			measurements.add(createDepth(5.0));
		}
		ITrackFileProcessor locationPreProcessor = new TestTrackProcessor(measurements,true);
		
		IFileTypeProcessingFactory fileTypeProcessingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(locationPreProcessor);
		EasyMock.replay(fileTypeProcessingFactory);
		
		TimeBasedTrackClustering timeBasedTrackClustering = new TimeBasedTrackClustering();
		timeBasedTrackClustering.bindFileTypeProcessingFactory(fileTypeProcessingFactory);
		
		TrackClusterResult classifyTracks = timeBasedTrackClustering.classifyTracks(trackFiles);
		assertTrue(classifyTracks.getCorruptTrackFiles().isEmpty());
		assertTrue(classifyTracks.getDuplicateTrackFiles().isEmpty());
		assertTrue(classifyTracks.getNodataTrackFiles().isEmpty());
		assertTrue(classifyTracks.getNoTimeMeasurementFiles().isEmpty());
		assertEquals(1, classifyTracks.getOrderedTrackFiles().size());
	}
	
	/**
	 * tests that data having absolute time information is classified correctly
	 * 
	 * @throws FilterException
	 */
	@Test
//	@Ignore
	public void testAbsoluteTimeDuplicateUpload() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFileA = new SimpleTrackFile();
		trackFiles.add(simpleTrackFileA);
		simpleTrackFileA.setAbsoluteTimeMeasurements(true);
		SimpleTrackFile simpleTrackFileB = new SimpleTrackFile();
		trackFiles.add(simpleTrackFileB);
		simpleTrackFileB.setAbsoluteTimeMeasurements(true);
		
		GeoBoundingBox geoBoundingBox = EasyMock.mock(GeoBoundingBox.class);
		EasyMock.expect(geoBoundingBox.getTop()).andReturn(45.0);
		EasyMock.expect(geoBoundingBox.getBottom()).andReturn(30.0);
		EasyMock.expect(geoBoundingBox.getLeft()).andReturn(10.0);
		EasyMock.expect(geoBoundingBox.getRight()).andReturn(20.0);
		
		final MeasuredPosition3D measuredPosition3DA = createPosition();
		final MeasuredPosition3D measuredPosition3DB = createPosition();
		List<Measurement> measurements = new ArrayList<>();

			measurements.add(measuredPosition3DA);
			measurements.add(createDepth(5.0));
			measurements.add(measuredPosition3DB);
		
		ITrackFileProcessor locationPreProcessor = new TestTrackProcessor(measurements,true);
		List<Measurement> measurements2 = new ArrayList<>();
		

		measurements2.add(measuredPosition3DA);
		measurements2.add(createDepth(5.0));
		measurements2.add(measuredPosition3DB);
		ITrackFileProcessor locationPreProcessor2 = new TestTrackProcessor(measurements2,true);
		
		IFileTypeProcessingFactory fileTypeProcessingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(locationPreProcessor).times(1).andReturn(locationPreProcessor2);
//		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(fileTypeProcessingFactory);
		
		TimeBasedTrackClustering timeBasedTrackClustering = new TimeBasedTrackClustering();
		timeBasedTrackClustering.bindFileTypeProcessingFactory(fileTypeProcessingFactory);
		
		TrackClusterResult classifyTracks = timeBasedTrackClustering.classifyTracks(trackFiles);
		assertTrue(classifyTracks.getCorruptTrackFiles().isEmpty());
		assertEquals(1, classifyTracks.getDuplicateTrackFiles().size());
		assertTrue(classifyTracks.getNodataTrackFiles().isEmpty());
		assertTrue(classifyTracks.getNoTimeMeasurementFiles().isEmpty());
		assertEquals(1, classifyTracks.getOrderedTrackFiles().size());
	}
	
	/**
	 * tests that data having absolute time information is classified correctly
	 * 
	 * @throws FilterException
	 */
	@Test
	public void testAbsoluteTimeSimpleCluster() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFileA = new SimpleTrackFile();
		simpleTrackFileA.setAbsoluteTimeMeasurements(true);
		trackFiles.add(simpleTrackFileA);
		SimpleTrackFile simpleTrackFileB = new SimpleTrackFile();
		simpleTrackFileB.setAbsoluteTimeMeasurements(true);
		trackFiles.add(simpleTrackFileB);
		
		GeoBoundingBox geoBoundingBox = EasyMock.mock(GeoBoundingBox.class);
		EasyMock.expect(geoBoundingBox.getTop()).andReturn(45.0);
		EasyMock.expect(geoBoundingBox.getBottom()).andReturn(30.0);
		EasyMock.expect(geoBoundingBox.getLeft()).andReturn(10.0);
		EasyMock.expect(geoBoundingBox.getRight()).andReturn(20.0);
		
		Calendar calendar = Calendar.getInstance();

		List<Measurement> measurements = new ArrayList<>();
		for(int i=0;i<10;i++) {
			calendar.add(Calendar.SECOND, 5);
			Date time = calendar.getTime();
			measurements.add(createPosition(54.1, 13.1, time));
			measurements.add(createDepth(5.0));
		}
		ITrackFileProcessor locationPreProcessor = new TestTrackProcessor(measurements,true);
		List<Measurement> measurements2 = new ArrayList<>();
		for(int i=0;i<10;i++) {
			calendar.add(Calendar.SECOND, 5);
			Date time = calendar.getTime();
			measurements2.add(createPosition(54.1, 13.1, time));
			measurements2.add(createDepth(5.0));
		}
		ITrackFileProcessor locationPreProcessor2 = new TestTrackProcessor(measurements2,true);

		IFileTypeProcessingFactory fileTypeProcessingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(locationPreProcessor).times(1).andReturn(locationPreProcessor2);
//		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(simpleTrackFileB)).andReturn(locationPreProcessor2);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(fileTypeProcessingFactory);
		
		TimeBasedTrackClustering timeBasedTrackClustering = new TimeBasedTrackClustering();
		timeBasedTrackClustering.bindFileTypeProcessingFactory(fileTypeProcessingFactory);
		
		TrackClusterResult classifyTracks = timeBasedTrackClustering.classifyTracks(trackFiles);
		assertTrue(classifyTracks.getCorruptTrackFiles().isEmpty());
		assertTrue(classifyTracks.getDuplicateTrackFiles().isEmpty());
		assertTrue(classifyTracks.getNodataTrackFiles().isEmpty());
		assertTrue(classifyTracks.getNoTimeMeasurementFiles().isEmpty());
		List<List<ITrackFile>> orderedTrackFiles = classifyTracks.getOrderedTrackFiles();
		assertEquals(1, orderedTrackFiles.size());
		assertEquals(2, orderedTrackFiles.get(0).size());
		assertEquals(simpleTrackFileA, orderedTrackFiles.get(0).get(0));
		assertEquals(simpleTrackFileB, orderedTrackFiles.get(0).get(1));
	}
	
	private Depth createDepth(double depthValue) {
		Depth depth = GeoFactory.eINSTANCE.createDepth();
		depth.setDepth(depthValue);
		depth.setValid(true);
		return depth;
	}
	
	public MeasuredPosition3D createPosition(double lat, double lon,Date time){
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		Latitude latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(lat);
		Longitude longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(lon);
		final MeasuredPosition3D measuredPosition3DA = geoFactory.createMeasuredPosition3D();
		measuredPosition3DA.setTime(time);
		measuredPosition3DA.setLatitude(latitude);
		measuredPosition3DA.setLongitude(longitude);
		measuredPosition3DA.setValid(true);
		return measuredPosition3DA;
	}

}
