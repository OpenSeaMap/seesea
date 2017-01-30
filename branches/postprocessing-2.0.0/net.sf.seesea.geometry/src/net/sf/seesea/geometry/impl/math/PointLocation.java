package net.sf.seesea.geometry.impl.math;

import java.util.List;

import net.sf.seesea.geometry.IPoint;
import net.sf.seesea.geometry.ITriangle;

public class PointLocation {

	public LocationResult getLocation(ITriangle triangle, IPoint point) {
		List<IPoint> points = triangle.getPoints();
		IPoint origin = points.get(0);
		IPoint destination = points.get(1);
		IPoint apex = points.get(2);
		if(apex.getX() == point.getX() && apex.getY() == point.getY()) {
			return new LocationResult(PointTriangleLocation.ONVERTEX, null);
		}
		if(destination.getX() == point.getX() && destination.getY() == point.getY()) {
			return new LocationResult(PointTriangleLocation.ONVERTEX, null);
		}
		if(origin.getX() == point.getX() && origin.getY() == point.getY()) {
			return new LocationResult(PointTriangleLocation.ONVERTEX, null);
		}
		
		ImpreciseGeometric impreciseGeometric = new ImpreciseGeometric();
		double destorientation = impreciseGeometric.counterclockwise(origin, destination, point);
		double originOrientation = impreciseGeometric.counterclockwise(destination, apex, point);
		double XoriginOrientation = impreciseGeometric.counterclockwise(apex, origin, point);

	    if (destorientation > 0.0) {
	    	return new LocationResult(PointTriangleLocation.OUTOFTRIANGLE, null);
//	    	if(originOrientation > 0.0) {
//	    	} else 
//	    	
		} else {
			if(originOrientation > 0.0) {
		    	return new LocationResult(PointTriangleLocation.OUTOFTRIANGLE, null);
			} else if(originOrientation == 0.0) {
				if(XoriginOrientation > 0.0) {
			    	return new LocationResult(PointTriangleLocation.OUTOFTRIANGLE, null);
				}
				return new LocationResult(PointTriangleLocation.ONEDGE, null);
			} else {
				if(destorientation == 0.0) {
					if(XoriginOrientation > 0.0) {
						return new LocationResult(PointTriangleLocation.OUTOFTRIANGLE, null);
					}
					return new LocationResult(PointTriangleLocation.ONEDGE, null);
				} else {
					if(XoriginOrientation > 0.0) {
						return new LocationResult(PointTriangleLocation.OUTOFTRIANGLE, null);
					} else if(XoriginOrientation == 0.0) {
						return new LocationResult(PointTriangleLocation.ONEDGE, null);
					}
					return new LocationResult(PointTriangleLocation.INTRIANGLE, null);
				}
			}
		}
	}
	
}

