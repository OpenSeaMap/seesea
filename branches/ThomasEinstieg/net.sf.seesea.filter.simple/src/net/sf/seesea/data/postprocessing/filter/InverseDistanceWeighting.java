package net.sf.seesea.data.postprocessing.filter;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.sf.seesea.model.core.geo.Depth;
import net.sf.seesea.model.core.geo.GeoPosition;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.physx.CompositeMeasurement;
import net.sf.seesea.model.util.GeoUtil;
import net.sf.seesea.track.api.ITrackPersistence;

public class InverseDistanceWeighting {

	private AtomicReference<ITrackPersistence> trackPersistenceAR = new AtomicReference<ITrackPersistence>();
	
	public boolean isValidPoint(MeasuredPosition3D measurement, Depth depth) {
		ITrackPersistence trackPersistence = trackPersistenceAR.get();
		List<CompositeMeasurement> depthPositons =	trackPersistence.getNearByPoints();
		DescriptiveStatistics ds = new DescriptiveStatistics();
		for (CompositeMeasurement compositeMeasurement : depthPositons) {
			double distance = 1/(1 +Math.abs(GeoUtil.getDistance(measurement, (GeoPosition) compositeMeasurement.getMeasurements().get(0))));
			Depth depth2 = (Depth) compositeMeasurement.getMeasurements().get(1);
			double depthDifference = Math.abs(depth2.getDepth() - depth.getDepth());
			// distanz klein -> depth 0 super
			// distanz hoch -> depth 0 ok, könnte falsch sein
			// distanz klein -> hohe abweichung -> einer von zweien ist falsch
			ds.addValue((distance * depthDifference) / depth.getDepth());
			System.out.println("InverseDistanceWeighting.isValidPoint()");
		}
//		double geometricMean = ds.getGeometricMean();
//		System.out.println(ds.toString());
		// low mean, low std dev
		if(ds.getMean() < 0.05 && ds.getStandardDeviation() < 0.001) {
			return true;
		}
		if(ds.getMean() >= 0.05 && ds.getStandardDeviation() < 0.001) {
			return false;
		}
		
		return false;
		// do calculation
		
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	public void bindTrackPersistence(ITrackPersistence trackPersistence) {
		trackPersistenceAR.set(trackPersistence);
	}

	public void unbindTrackPersistence(ITrackPersistence trackPersistence) {
		trackPersistenceAR.compareAndSet(null, trackPersistence);
	}

}
