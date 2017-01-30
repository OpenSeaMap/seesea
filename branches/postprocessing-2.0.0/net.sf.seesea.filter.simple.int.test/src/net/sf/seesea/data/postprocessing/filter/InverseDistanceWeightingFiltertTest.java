package net.sf.seesea.data.postprocessing.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import net.sf.seesea.model.core.geo.Depth;
import net.sf.seesea.model.core.geo.GeoFactory;
import net.sf.seesea.model.core.geo.Latitude;
import net.sf.seesea.model.core.geo.Longitude;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.physx.CompositeMeasurement;
import net.sf.seesea.model.core.physx.PhysxFactory;
import net.sf.seesea.track.api.ITrackPersistence;

public class InverseDistanceWeightingFiltertTest {

	@Test
	public void testFalseMeasurementWithLowVarianceAroundMeasuredPoint() {
		// sequence of measurement values were on is suspicious
		// call a check for that one
		// many near distances should give positive result. data should fade over time
		// and probably based on position
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();

		List<CompositeMeasurement> nearbyMeasures = new ArrayList<>();
		final MeasuredPosition3D posA = createPosition(time, 9.99, 29.99);
		Depth depthA = createDepth(11.0);
		createAndAddCompositeMeasurement(nearbyMeasures, posA, depthA);
		final MeasuredPosition3D posB = createPosition(time, 9.99, 30.01);
		Depth depthB = createDepth(11.0);
		createAndAddCompositeMeasurement(nearbyMeasures, posB, depthB);
		final MeasuredPosition3D posC = createPosition(time, 10.01, 29.99);
		Depth depthC = createDepth(11.0);
		createAndAddCompositeMeasurement(nearbyMeasures, posC, depthC);
		final MeasuredPosition3D posD = createPosition(time, 10.01, 30.01);
		Depth depthD = createDepth(11.0);
		createAndAddCompositeMeasurement(nearbyMeasures, posD, depthD);

		InverseDistanceWeighting inverseDistanceWeighting = new InverseDistanceWeighting();
		ITrackPersistence trackPersistence = EasyMock.createNiceMock(ITrackPersistence.class);
		EasyMock.expect(trackPersistence.getNearByPoints()).andReturn(nearbyMeasures);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackPersistence);
		inverseDistanceWeighting.bindTrackPersistence(trackPersistence);
		
		final MeasuredPosition3D pos = createPosition(time, 10.00, 30.00);
		Depth depth = createDepth(10.0);
		
		assertFalse(inverseDistanceWeighting.isValidPoint(pos, depth));
	}

	private void createAndAddCompositeMeasurement(List<CompositeMeasurement> nearbyMeasures, final MeasuredPosition3D posA, Depth depthA) {
		CompositeMeasurement compositeMeasurement = PhysxFactory.eINSTANCE.createCompositeMeasurement();
		compositeMeasurement.getMeasurements().add(posA);
		compositeMeasurement.getMeasurements().add(depthA);
		nearbyMeasures.add(compositeMeasurement);
	}

	@Test
	public void testEqualPointSmallVariance() {
		// sequence of measurement values were on is suspicious
		// call a check for that one
		// many near distances should give positive result. data should fade over time
		// and probably based on position
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();

		List<CompositeMeasurement> nearbyMeasures = new ArrayList<>();
		final MeasuredPosition3D posA = createPosition(time, 10, 30.0);
		Depth depthA = createDepth(10.02);
		createAndAddCompositeMeasurement(nearbyMeasures, posA, depthA);
		final MeasuredPosition3D posB = createPosition(time, 10, 30.0);
		Depth depthB = createDepth(10.01);
		createAndAddCompositeMeasurement(nearbyMeasures, posB, depthB);
		final MeasuredPosition3D posC = createPosition(time, 10, 30.0);
		Depth depthC = createDepth(9.99);
		createAndAddCompositeMeasurement(nearbyMeasures, posC, depthC);
		final MeasuredPosition3D posD = createPosition(time, 10, 30.0);
		Depth depthD = createDepth(9.98);
		createAndAddCompositeMeasurement(nearbyMeasures, posD, depthD);

		InverseDistanceWeighting inverseDistanceWeighting = new InverseDistanceWeighting();
		ITrackPersistence trackPersistence = EasyMock.createNiceMock(ITrackPersistence.class);
		EasyMock.expect(trackPersistence.getNearByPoints()).andReturn(nearbyMeasures);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackPersistence);
		inverseDistanceWeighting.bindTrackPersistence(trackPersistence);
		
		final MeasuredPosition3D pos = createPosition(time, 10.00, 30.00);
		Depth depth = createDepth(10.0);
		
		assertTrue(inverseDistanceWeighting.isValidPoint(pos, depth));
	}

	@Test
	public void testEqualPointNoVariance() {
		// sequence of measurement values were on is suspicious
		// call a check for that one
		// many near distances should give positive result. data should fade over time
		// and probably based on position
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();

		List<CompositeMeasurement> nearbyMeasures = new ArrayList<>();
		final MeasuredPosition3D posA = createPosition(time, 10, 30.0);
		Depth depthA = createDepth(10.0);
		createAndAddCompositeMeasurement(nearbyMeasures, posA, depthA);
		final MeasuredPosition3D posB = createPosition(time, 10, 30.0);
		Depth depthB = createDepth(10.0);
		createAndAddCompositeMeasurement(nearbyMeasures, posB, depthB);
		final MeasuredPosition3D posC = createPosition(time, 10, 30.0);
		Depth depthC = createDepth(10.0);
		createAndAddCompositeMeasurement(nearbyMeasures, posC, depthC);
		final MeasuredPosition3D posD = createPosition(time, 10, 30.0);
		Depth depthD = createDepth(10.0);
		createAndAddCompositeMeasurement(nearbyMeasures, posD, depthD);

		InverseDistanceWeighting inverseDistanceWeighting = new InverseDistanceWeighting();
		ITrackPersistence trackPersistence = EasyMock.createNiceMock(ITrackPersistence.class);
		EasyMock.expect(trackPersistence.getNearByPoints()).andReturn(nearbyMeasures);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackPersistence);
		inverseDistanceWeighting.bindTrackPersistence(trackPersistence);
		
		final MeasuredPosition3D pos = createPosition(time, 10.00, 30.00);
		Depth depth = createDepth(10.0);
		
		assertTrue(inverseDistanceWeighting.isValidPoint(pos, depth));
	}

	private Depth createDepth(double depthValue) {
		Depth depth = GeoFactory.eINSTANCE.createDepth();
		depth.setDepth(depthValue);
		return depth;
	}

	private MeasuredPosition3D createPosition(Date time, double latitudeDeg,double longitudeDefg) {
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		Latitude latitude = geoFactory.createLatitude();
		latitude.setDecimalDegree(latitudeDeg);
		Longitude longitude = geoFactory.createLongitude();
		longitude.setDecimalDegree(longitudeDefg);
		final MeasuredPosition3D measuredPosition3DA = geoFactory.createMeasuredPosition3D();
		measuredPosition3DA.setTime(time);
		measuredPosition3DA.setLatitude(latitude);
		measuredPosition3DA.setLongitude(longitude);
		return measuredPosition3DA;
	}
	
}




