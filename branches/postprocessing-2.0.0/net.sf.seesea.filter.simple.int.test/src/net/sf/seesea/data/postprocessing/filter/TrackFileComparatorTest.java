package net.sf.seesea.data.postprocessing.filter;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.easymock.EasyMock;
import org.junit.Test;

import net.sf.seesea.model.core.geo.GeoFactory;
import net.sf.seesea.model.core.geo.Latitude;
import net.sf.seesea.model.core.geo.Longitude;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.track.api.data.ITrackFile;

public class TrackFileComparatorTest {

	@Test
	public void testSimpleTimeTotalOrdering() {
		
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		calendar.add(Calendar.SECOND, 1);
		Date time2 = calendar.getTime();
		calendar.add(Calendar.SECOND, 1);
		Date time3 = calendar.getTime();
		calendar.add(Calendar.SECOND, 1);
		Date time4 = calendar.getTime();

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
		latitude.setDecimalDegree(54.1);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.1);
		measuredPosition3DB.setLatitude(latitude);
		measuredPosition3DB.setLongitude(longitude);

		latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.1);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.1);
		final MeasuredPosition3D measuredPosition3DA1 = geoFactory.createMeasuredPosition3D();
		measuredPosition3DA1.setTime(time3);
		measuredPosition3DA1.setLatitude(latitude);
		measuredPosition3DA1.setLongitude(longitude);
		final MeasuredPosition3D measuredPosition3DB1 = geoFactory.createMeasuredPosition3D();
		measuredPosition3DB1.setTime(time4);
		latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.2);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.2);
		measuredPosition3DB1.setLatitude(latitude);
		measuredPosition3DB1.setLongitude(longitude);

		ITrackFile trackFileA = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFileA.getStart()).andReturn(measuredPosition3DA);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(trackFileA.getEnd()).andReturn(measuredPosition3DB);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackFileA);
		
		ITrackFile trackFileB = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFileB.getStart()).andReturn(measuredPosition3DA1);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(trackFileB.getEnd()).andReturn(measuredPosition3DB1);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackFileB);
		
		TrackFileComparator trackFileComparator = new TrackFileComparator();
		int compare = trackFileComparator.compare(trackFileA, trackFileB);
		assertTrue("trackA should be before trackB", compare < 0 );
		compare = trackFileComparator.compare(trackFileB,trackFileA);
		assertTrue("trackA should be before trackB", compare > 0 );
	}
	
	@Test
	public void testEqualTimeTotalOrdering() {
		
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		calendar.add(Calendar.SECOND, 1);
		Date time2 = calendar.getTime();
//		calendar.add(Calendar.SECOND, 1);
//		Date time3 = calendar.getTime();
//		calendar.add(Calendar.SECOND, 1);
//		Date time4 = calendar.getTime();

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
		latitude.setDecimalDegree(54.1);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.1);
		measuredPosition3DB.setLatitude(latitude);
		measuredPosition3DB.setLongitude(longitude);

		latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.1);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.1);
		final MeasuredPosition3D measuredPosition3DA1 = geoFactory.createMeasuredPosition3D();
		measuredPosition3DA1.setTime(time);
		measuredPosition3DA1.setLatitude(latitude);
		measuredPosition3DA1.setLongitude(longitude);
		final MeasuredPosition3D measuredPosition3DB1 = geoFactory.createMeasuredPosition3D();
		measuredPosition3DB1.setTime(time2);
		latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.2);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.2);
		measuredPosition3DB1.setLatitude(latitude);
		measuredPosition3DB1.setLongitude(longitude);

		ITrackFile trackFileA = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFileA.getStart()).andReturn(measuredPosition3DA);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(trackFileA.getTrackQualifier()).andReturn("A");
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(trackFileA.getEnd()).andReturn(measuredPosition3DB);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackFileA);
		
		ITrackFile trackFileB = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFileB.getTrackQualifier()).andReturn("B");
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(trackFileB.getStart()).andReturn(measuredPosition3DA1);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(trackFileB.getEnd()).andReturn(measuredPosition3DB1);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackFileB);
		
		TrackFileComparator trackFileComparator = new TrackFileComparator();
		int compare = trackFileComparator.compare(trackFileA, trackFileB);
		assertTrue("trackA should be before trackB", compare < 0 );
		compare = trackFileComparator.compare(trackFileB,trackFileA);
		assertTrue("trackA should be before trackB", compare > 0 );
	}
	
	@Test
	public void testOverlappingTimeTotalOrdering() {
		
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		calendar.add(Calendar.SECOND, 2);
		Date time2 = calendar.getTime();
		calendar.add(Calendar.SECOND, -1);
		Date time3 = calendar.getTime();
		calendar.add(Calendar.SECOND, 2);
		Date time4 = calendar.getTime();

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
		latitude.setDecimalDegree(54.1);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.1);
		measuredPosition3DB.setLatitude(latitude);
		measuredPosition3DB.setLongitude(longitude);

		latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.1);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.1);
		final MeasuredPosition3D measuredPosition3DA1 = geoFactory.createMeasuredPosition3D();
		measuredPosition3DA1.setTime(time3);
		measuredPosition3DA1.setLatitude(latitude);
		measuredPosition3DA1.setLongitude(longitude);
		final MeasuredPosition3D measuredPosition3DB1 = geoFactory.createMeasuredPosition3D();
		measuredPosition3DB1.setTime(time4);
		latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(54.2);
		longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(13.2);
		measuredPosition3DB1.setLatitude(latitude);
		measuredPosition3DB1.setLongitude(longitude);

		ITrackFile trackFileA = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFileA.getStart()).andReturn(measuredPosition3DA);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(trackFileA.getTrackQualifier()).andReturn("A");
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(trackFileA.getEnd()).andReturn(measuredPosition3DB);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackFileA);
		
		ITrackFile trackFileB = EasyMock.createNiceMock(ITrackFile.class);
		EasyMock.expect(trackFileB.getTrackQualifier()).andReturn("B");
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(trackFileB.getStart()).andReturn(measuredPosition3DA1);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(trackFileB.getEnd()).andReturn(measuredPosition3DB1);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackFileB);
		
		TrackFileComparator trackFileComparator = new TrackFileComparator();
		int compare = trackFileComparator.compare(trackFileA, trackFileB);
		assertTrue("trackA should be before trackB", compare < 0 );
		compare = trackFileComparator.compare(trackFileB,trackFileA);
		assertTrue("trackA should be before trackB", compare > 0 );
	}
	
}
