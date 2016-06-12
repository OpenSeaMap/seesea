package net.sf.seesea.geometry.impl.math;

import java.util.List;

import net.sf.seesea.geometry.ITriangle;

public class LocationResult {

	private PointTriangleLocation pointTriangleLocation;
	
	private List<ITriangle> triangle;

	public LocationResult(PointTriangleLocation pointTriangleLocation, List<ITriangle> triangle) {
		super();
		this.pointTriangleLocation = pointTriangleLocation;
		this.triangle = triangle;
	}

	public PointTriangleLocation getPointTriangleLocation() {
		return pointTriangleLocation;
	}

	public void setPointTriangleLocation(PointTriangleLocation pointTriangleLocation) {
		this.pointTriangleLocation = pointTriangleLocation;
	}

	public List<ITriangle> getTriangle() {
		return triangle;
	}

	public void setTriangle(List<ITriangle> triangle) {
		this.triangle = triangle;
	}
	
	
}
