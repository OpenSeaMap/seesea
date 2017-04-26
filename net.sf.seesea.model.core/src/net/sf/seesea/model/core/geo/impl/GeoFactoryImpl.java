/**
 * <copyright>
Copyright (c) 2010-2012, Jens Kübler
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * </copyright>
 *
 * $Id$
 */
package net.sf.seesea.model.core.geo.impl;

import net.sf.seesea.model.core.geo.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GeoFactoryImpl extends EFactoryImpl implements GeoFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final GeoFactoryImpl eINSTANCE = init();

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static GeoFactoryImpl init() {
		try {
			GeoFactoryImpl theGeoFactory = (GeoFactoryImpl)EPackage.Registry.INSTANCE.getEFactory(GeoPackageImpl.eNS_URI);
			if (theGeoFactory != null) {
				return theGeoFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new GeoFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeoFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case GeoPackageImpl.GEO_POSITION: return (EObject)createGeoPosition();
			case GeoPackageImpl.COORDINATE: return (EObject)createCoordinate();
			case GeoPackageImpl.LATITUDE: return (EObject)createLatitude();
			case GeoPackageImpl.LONGITUDE: return (EObject)createLongitude();
			case GeoPackageImpl.ROUTE: return (EObject)createRoute();
			case GeoPackageImpl.NAMED_ARTIFACT: return (EObject)createNamedArtifact();
			case GeoPackageImpl.NAMED_POSITION: return (EObject)createNamedPosition();
			case GeoPackageImpl.ROUTING_CONTAINER: return (EObject)createRoutingContainer();
			case GeoPackageImpl.POI_CONTAINER: return (EObject)createPOIContainer();
			case GeoPackageImpl.CHART_CONTAINER: return (EObject)createChartContainer();
			case GeoPackageImpl.NAVIGATION_COMPOUND: return (EObject)createNavigationCompound();
			case GeoPackageImpl.CHART: return (EObject)createChart();
			case GeoPackageImpl.GEO_POSITION3_D: return (EObject)createGeoPosition3D();
			case GeoPackageImpl.MEASURED_POSITION3_D: return (EObject)createMeasuredPosition3D();
			case GeoPackageImpl.TRACKS_CONTAINER: return (EObject)createTracksContainer();
			case GeoPackageImpl.TRACK: return (EObject)createTrack();
			case GeoPackageImpl.CHART_SYMBOL: return (EObject)createChartSymbol();
			case GeoPackageImpl.CHART_AREA: return (EObject)createChartArea();
			case GeoPackageImpl.CHART_WAY: return (EObject)createChartWay();
			case GeoPackageImpl.NAVAREA: return (EObject)createNavarea();
			case GeoPackageImpl.DEPTH: return (EObject)createDepth();
			case GeoPackageImpl.GNSS_MEASURED_POSITION: return (EObject)createGNSSMeasuredPosition();
			case GeoPackageImpl.ANCHOR_POSITION: return (EObject)createAnchorPosition();
			case GeoPackageImpl.GEO_BOUNDING_BOX: return (EObject)createGeoBoundingBox();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case GeoPackageImpl.DIRECTION:
				return createDirectionFromString(eDataType, initialValue);
			case GeoPackageImpl.LATITUDE_HEMISPHERE:
				return createLatitudeHemisphereFromString(eDataType, initialValue);
			case GeoPackageImpl.LONGITUDE_HEMISPHERE:
				return createLongitudeHemisphereFromString(eDataType, initialValue);
			case GeoPackageImpl.RELATIVE_DEPTH_MEASUREMENT_POSITION:
				return createRelativeDepthMeasurementPositionFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case GeoPackageImpl.DIRECTION:
				return convertDirectionToString(eDataType, instanceValue);
			case GeoPackageImpl.LATITUDE_HEMISPHERE:
				return convertLatitudeHemisphereToString(eDataType, instanceValue);
			case GeoPackageImpl.LONGITUDE_HEMISPHERE:
				return convertLongitudeHemisphereToString(eDataType, instanceValue);
			case GeoPackageImpl.RELATIVE_DEPTH_MEASUREMENT_POSITION:
				return convertRelativeDepthMeasurementPositionToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeoPosition createGeoPosition() {
		GeoPositionImpl geoPosition = new GeoPositionImpl();
		return geoPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Coordinate createCoordinate() {
		CoordinateImpl coordinate = new CoordinateImpl();
		return coordinate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Latitude createLatitude() {
		LatitudeImpl latitude = new LatitudeImpl();
		return latitude;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Longitude createLongitude() {
		LongitudeImpl longitude = new LongitudeImpl();
		return longitude;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Route createRoute() {
		RouteImpl route = new RouteImpl();
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NamedArtifact createNamedArtifact() {
		NamedArtifactImpl namedArtifact = new NamedArtifactImpl();
		return namedArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NamedPosition createNamedPosition() {
		NamedPositionImpl namedPosition = new NamedPositionImpl();
		return namedPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RoutingContainer createRoutingContainer() {
		RoutingContainerImpl routingContainer = new RoutingContainerImpl();
		return routingContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public POIContainer createPOIContainer() {
		POIContainerImpl poiContainer = new POIContainerImpl();
		return poiContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Navarea createNavarea() {
		NavareaImpl navarea = new NavareaImpl();
		return navarea;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Depth createDepth() {
		DepthImpl depth = new DepthImpl();
		return depth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GNSSMeasuredPosition createGNSSMeasuredPosition() {
		GNSSMeasuredPositionImpl gnssMeasuredPosition = new GNSSMeasuredPositionImpl();
		return gnssMeasuredPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnchorPosition createAnchorPosition() {
		AnchorPositionImpl anchorPosition = new AnchorPositionImpl();
		return anchorPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeoBoundingBox createGeoBoundingBox() {
		GeoBoundingBoxImpl geoBoundingBox = new GeoBoundingBoxImpl();
		return geoBoundingBox;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Direction createDirectionFromString(EDataType eDataType, String initialValue) {
		Direction result = Direction.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDirectionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LatitudeHemisphere createLatitudeHemisphereFromString(EDataType eDataType, String initialValue) {
		LatitudeHemisphere result = LatitudeHemisphere.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLatitudeHemisphereToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LongitudeHemisphere createLongitudeHemisphereFromString(EDataType eDataType, String initialValue) {
		LongitudeHemisphere result = LongitudeHemisphere.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLongitudeHemisphereToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RelativeDepthMeasurementPosition createRelativeDepthMeasurementPositionFromString(EDataType eDataType, String initialValue) {
		RelativeDepthMeasurementPosition result = RelativeDepthMeasurementPosition.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRelativeDepthMeasurementPositionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChartContainer createChartContainer() {
		ChartContainerImpl chartContainer = new ChartContainerImpl();
		return chartContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NavigationCompound createNavigationCompound() {
		NavigationCompoundImpl navigationCompound = new NavigationCompoundImpl();
		return navigationCompound;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Chart createChart() {
		ChartImpl chart = new ChartImpl();
		return chart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeoPosition3D createGeoPosition3D() {
		GeoPosition3DImpl geoPosition3D = new GeoPosition3DImpl();
		return geoPosition3D;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MeasuredPosition3D createMeasuredPosition3D() {
		MeasuredPosition3DImpl measuredPosition3D = new MeasuredPosition3DImpl();
		return measuredPosition3D;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TracksContainer createTracksContainer() {
		TracksContainerImpl tracksContainer = new TracksContainerImpl();
		return tracksContainer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Track createTrack() {
		TrackImpl track = new TrackImpl();
		return track;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChartSymbol createChartSymbol() {
		ChartSymbolImpl chartSymbol = new ChartSymbolImpl();
		return chartSymbol;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChartArea createChartArea() {
		ChartAreaImpl chartArea = new ChartAreaImpl();
		return chartArea;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChartWay createChartWay() {
		ChartWayImpl chartWay = new ChartWayImpl();
		return chartWay;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeoPackageImpl getGeoPackage() {
		return (GeoPackageImpl)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static GeoPackageImpl getPackage() {
		return GeoPackageImpl.eINSTANCE;
	}

} //GeoFactoryImpl
