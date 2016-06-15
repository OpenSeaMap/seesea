package net.sf.seesea.data.postprocessing.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Ignore;
import org.junit.Test;

import net.sf.seesea.data.postprocessing.process.FilterException;
import net.sf.seesea.data.postprocessing.process.IDepthPositionPreProcessor;
import net.sf.seesea.data.postprocessing.process.IFileTypeProcessingFactory;
import net.sf.seesea.data.postprocessing.process.TrackClusterResult;
import net.sf.seesea.model.core.geo.GeoBoundingBox;
import net.sf.seesea.model.core.geo.GeoFactory;
import net.sf.seesea.model.core.geo.Latitude;
import net.sf.seesea.model.core.geo.Longitude;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
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

		IDepthPositionPreProcessor depthPositionPreProcessor = EasyMock.createNiceMock(IDepthPositionPreProcessor.class);
		EasyMock.expect(depthPositionPreProcessor.hasDepthData()).andReturn(false);
		EasyMock.expectLastCall().times(1);
		EasyMock.replay(depthPositionPreProcessor);

		ITrackFileProcessor locationPreProcessor = EasyMock.createNiceMock(ITrackFileProcessor.class);
		EasyMock.replay(locationPreProcessor);
		
		IFileTypeProcessingFactory fileTypeProcessingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(locationPreProcessor);
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

		ITrackFileProcessor locationPreProcessor = EasyMock.createNiceMock(ITrackFileProcessor.class);
		EasyMock.replay(locationPreProcessor);

		IDepthPositionPreProcessor depthPreProcessor = EasyMock.createNiceMock(IDepthPositionPreProcessor.class);
		EasyMock.expect(depthPreProcessor.getBoundingBox()).andReturn(geoBoundingBox);
		EasyMock.expectLastCall().times(1);
		EasyMock.expect(depthPreProcessor.hasDepthData()).andReturn(true);
		EasyMock.expectLastCall().times(1);
		EasyMock.expect(depthPreProcessor.getPointCount()).andReturn(100L);
		EasyMock.expectLastCall().times(1);
		EasyMock.replay(depthPreProcessor);
		
		
		IFileTypeProcessingFactory fileTypeProcessingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(locationPreProcessor);
		EasyMock.replay(fileTypeProcessingFactory);
		
		TimeBasedTrackClustering timeBasedTrackClustering = new TimeBasedTrackClustering();
		timeBasedTrackClustering.bindFileTypeProcessingFactory(fileTypeProcessingFactory);
		
		TrackClusterResult classifyTracks = timeBasedTrackClustering.classifyTracks(trackFiles);
		assertFalse(classifyTracks.getNoTimeMeasurementFiles().isEmpty());
		
		
	}
	
	/**
	 * tests that data having relative time information is classified correctly
	 * 
	 * @throws FilterException
	 */
	@Test
	public void testTimeDepthDataClassifyRelative() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFile = new SimpleTrackFile();
		trackFiles.add(simpleTrackFile);
		
		GeoBoundingBox geoBoundingBox = EasyMock.mock(GeoBoundingBox.class);
		EasyMock.expect(geoBoundingBox.getTop()).andReturn(45.0);
		EasyMock.expect(geoBoundingBox.getBottom()).andReturn(30.0);
		EasyMock.expect(geoBoundingBox.getLeft()).andReturn(10.0);
		EasyMock.expect(geoBoundingBox.getRight()).andReturn(20.0);
		
		MeasuredPosition3D measuredPosition3D = GeoFactory.eINSTANCE.createMeasuredPosition3D();
		
		ITrackFileProcessor locationPreProcessor = EasyMock.createNiceMock(ITrackFileProcessor.class);
		EasyMock.replay(locationPreProcessor);

		IDepthPositionPreProcessor depthPreProcessor = EasyMock.createNiceMock(IDepthPositionPreProcessor.class);
		EasyMock.expect(depthPreProcessor.getBoundingBox()).andReturn(geoBoundingBox);
		EasyMock.expectLastCall().times(1);
		EasyMock.expect(depthPreProcessor.hasDepthData()).andReturn(true);
		EasyMock.expectLastCall().times(1);
		EasyMock.expect(depthPreProcessor.getPointCount()).andReturn(100L);
		EasyMock.expectLastCall().times(1);
		EasyMock.expect(depthPreProcessor.hasRelativeTimedMeasurements()).andReturn(true);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor.getStart()).andReturn(measuredPosition3D);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(depthPreProcessor);
		
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
	public void testTimeDepthDataClassifyAbsolute() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFile = new SimpleTrackFile();
		trackFiles.add(simpleTrackFile);
		
		GeoBoundingBox geoBoundingBox = EasyMock.mock(GeoBoundingBox.class);
		EasyMock.expect(geoBoundingBox.getTop()).andReturn(45.0);
		EasyMock.expect(geoBoundingBox.getBottom()).andReturn(30.0);
		EasyMock.expect(geoBoundingBox.getLeft()).andReturn(10.0);
		EasyMock.expect(geoBoundingBox.getRight()).andReturn(20.0);
		
		MeasuredPosition3D measuredPosition3D = GeoFactory.eINSTANCE.createMeasuredPosition3D();
		
		ITrackFileProcessor locationPreProcessor = EasyMock.createNiceMock(ITrackFileProcessor.class);
		EasyMock.replay(locationPreProcessor);
		
		IDepthPositionPreProcessor depthPreProcessor = EasyMock.createNiceMock(IDepthPositionPreProcessor.class);
		EasyMock.expect(depthPreProcessor.getBoundingBox()).andReturn(geoBoundingBox);
		EasyMock.expectLastCall().times(1);
		EasyMock.expect(depthPreProcessor.hasDepthData()).andReturn(true);
		EasyMock.expectLastCall().times(1);
		EasyMock.expect(depthPreProcessor.getPointCount()).andReturn(100L);
		EasyMock.expectLastCall().times(1);
		EasyMock.expect(depthPreProcessor.hasAbsoluteTimedMeasurements()).andReturn(true);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor.getStart()).andReturn(measuredPosition3D);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(depthPreProcessor);
		
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
	public void testAbsoluteTimeDuplicateUpload() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFileA = new SimpleTrackFile();
		trackFiles.add(simpleTrackFileA);
		SimpleTrackFile simpleTrackFileB = new SimpleTrackFile();
		trackFiles.add(simpleTrackFileB);
		
		GeoBoundingBox geoBoundingBox = EasyMock.mock(GeoBoundingBox.class);
		EasyMock.expect(geoBoundingBox.getTop()).andReturn(45.0);
		EasyMock.expect(geoBoundingBox.getBottom()).andReturn(30.0);
		EasyMock.expect(geoBoundingBox.getLeft()).andReturn(10.0);
		EasyMock.expect(geoBoundingBox.getRight()).andReturn(20.0);
		
		Date time = Calendar.getInstance().getTime();
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		Latitude latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.1);
		Longitude longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.1);
		final MeasuredPosition3D measuredPosition3DA = geoFactory.createMeasuredPosition3D();
		measuredPosition3DA.setTime(time);
		measuredPosition3DA.setLatitude(latitude);
		measuredPosition3DA.setLongitude(longitude);
		final MeasuredPosition3D measuredPosition3DB = geoFactory.createMeasuredPosition3D();
		measuredPosition3DB.setTime(time);
		latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.1);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.1);
		measuredPosition3DB.setLatitude(latitude);
		measuredPosition3DB.setLongitude(longitude);
		
		ITrackFileProcessor locationPreProcessor = EasyMock.createNiceMock(ITrackFileProcessor.class);
		EasyMock.replay(locationPreProcessor);
		
		IDepthPositionPreProcessor depthPreProcessor = EasyMock.createNiceMock(IDepthPositionPreProcessor.class);
		EasyMock.expect(depthPreProcessor.getBoundingBox()).andReturn(geoBoundingBox);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor.hasDepthData()).andReturn(true);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor.getPointCount()).andReturn(100L);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor.hasAbsoluteTimedMeasurements()).andReturn(true);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor.getStart()).andAnswer(new IAnswer<MeasuredPosition3D>() {

			int i = 0;
			
			@Override
			public MeasuredPosition3D answer() throws Throwable {
				if(i==0) {
					i++;
					return measuredPosition3DA;
				}
				return measuredPosition3DB;
			}
		});
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(depthPreProcessor);
		
		IFileTypeProcessingFactory fileTypeProcessingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(locationPreProcessor);
		EasyMock.expectLastCall().anyTimes();
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
	@Ignore
	public void testAbsoluteTimeSimpleCluster() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFileA = new SimpleTrackFile();
		trackFiles.add(simpleTrackFileA);
		SimpleTrackFile simpleTrackFileB = new SimpleTrackFile();
		trackFiles.add(simpleTrackFileB);
		
		GeoBoundingBox geoBoundingBox = EasyMock.mock(GeoBoundingBox.class);
		EasyMock.expect(geoBoundingBox.getTop()).andReturn(45.0);
		EasyMock.expect(geoBoundingBox.getBottom()).andReturn(30.0);
		EasyMock.expect(geoBoundingBox.getLeft()).andReturn(10.0);
		EasyMock.expect(geoBoundingBox.getRight()).andReturn(20.0);
		
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		calendar.add(Calendar.MINUTE, 5);
		Date time2 = calendar.getTime();
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		Latitude latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.1);
		Longitude longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.1);
		final MeasuredPosition3D measuredPosition3DA = geoFactory.createMeasuredPosition3D();
		measuredPosition3DA.setTime(time);
		measuredPosition3DA.setLatitude(latitude);
		measuredPosition3DA.setLongitude(longitude);
		final MeasuredPosition3D measuredPosition3DB = geoFactory.createMeasuredPosition3D();
		measuredPosition3DB.setTime(time2);
		latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.2);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.2);
		measuredPosition3DB.setLatitude(latitude);
		measuredPosition3DB.setLongitude(longitude);
		
		ITrackFileProcessor locationPreProcessor = EasyMock.createNiceMock(ITrackFileProcessor.class);
		EasyMock.replay(locationPreProcessor);
		
		IDepthPositionPreProcessor depthPreProcessor = EasyMock.createNiceMock(IDepthPositionPreProcessor.class);
		EasyMock.expect(depthPreProcessor.getBoundingBox()).andReturn(geoBoundingBox);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor.hasDepthData()).andReturn(true);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor.getPointCount()).andReturn(100L);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor.hasAbsoluteTimedMeasurements()).andReturn(true);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor.getStart()).andReturn(measuredPosition3DA);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(depthPreProcessor);

		IDepthPositionPreProcessor depthPreProcessor2 = EasyMock.createNiceMock(IDepthPositionPreProcessor.class);
		EasyMock.expect(depthPreProcessor2.getBoundingBox()).andReturn(geoBoundingBox);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor2.hasDepthData()).andReturn(true);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor2.getPointCount()).andReturn(100L);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor2.hasAbsoluteTimedMeasurements()).andReturn(true);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(depthPreProcessor2.getStart()).andReturn(measuredPosition3DB);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(depthPreProcessor2);

		IFileTypeProcessingFactory fileTypeProcessingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(simpleTrackFileA)).andReturn(locationPreProcessor);
		EasyMock.expect(fileTypeProcessingFactory.createLocationPreProcessor(simpleTrackFileB)).andReturn(locationPreProcessor);
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
}
