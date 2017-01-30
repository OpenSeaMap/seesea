package net.sf.seesea.provider.navigation.nmea.v183;

import net.sf.seesea.model.core.geo.Coordinate;

class PrecisionCoordinate {

	public PrecisionCoordinate(Coordinate coordinate, int precision) {
		super();
		this.coordinate = coordinate;
		this.precision = precision;
	}

	private Coordinate coordinate;

	private int precision;

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public int getPrecision() {
		return precision;
	}
	
	
}