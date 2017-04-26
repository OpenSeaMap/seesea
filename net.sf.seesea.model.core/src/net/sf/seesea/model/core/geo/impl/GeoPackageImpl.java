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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import net.sf.seesea.model.core.data.impl.DataPackageImpl;
import net.sf.seesea.model.core.diagramInterchange.impl.DiagramInterchangePackageImpl;
import net.sf.seesea.model.core.geo.AnchorPosition;
import net.sf.seesea.model.core.geo.Chart;
import net.sf.seesea.model.core.geo.ChartArea;
import net.sf.seesea.model.core.geo.ChartContainer;
import net.sf.seesea.model.core.geo.ChartSymbol;
import net.sf.seesea.model.core.geo.ChartWay;
import net.sf.seesea.model.core.geo.Coordinate;
import net.sf.seesea.model.core.geo.Depth;
import net.sf.seesea.model.core.geo.Direction;
import net.sf.seesea.model.core.geo.GNSSMeasuredPosition;
import net.sf.seesea.model.core.geo.GeoBoundingBox;
import net.sf.seesea.model.core.geo.GeoFactory;
import net.sf.seesea.model.core.geo.GeoPosition;
import net.sf.seesea.model.core.geo.GeoPosition3D;
import net.sf.seesea.model.core.geo.Latitude;
import net.sf.seesea.model.core.geo.LatitudeHemisphere;
import net.sf.seesea.model.core.geo.Longitude;
import net.sf.seesea.model.core.geo.LongitudeHemisphere;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.geo.NamedArtifact;
import net.sf.seesea.model.core.geo.NamedPosition;
import net.sf.seesea.model.core.geo.Navarea;
import net.sf.seesea.model.core.geo.NavigationCompound;
import net.sf.seesea.model.core.geo.POIContainer;
import net.sf.seesea.model.core.geo.RelativeDepthMeasurementPosition;
import net.sf.seesea.model.core.geo.Route;
import net.sf.seesea.model.core.geo.RoutingContainer;
import net.sf.seesea.model.core.geo.Track;
import net.sf.seesea.model.core.geo.TracksContainer;
import net.sf.seesea.model.core.geo.osm.impl.OsmPackageImpl;
import net.sf.seesea.model.core.impl.CorePackageImpl;
import net.sf.seesea.model.core.physx.impl.PhysxPackageImpl;
import net.sf.seesea.model.core.weather.impl.WeatherPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @see net.sf.seesea.model.core.geo.GeoFactory
 * @model kind="package"
 * @generated
 */
public class GeoPackageImpl extends EPackageImpl {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNAME = "geo"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_URI = "b"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_PREFIX = "b"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final GeoPackageImpl eINSTANCE = net.sf.seesea.model.core.geo.impl.GeoPackageImpl.init();

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.GeoPositionImpl <em>Position</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.GeoPositionImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getGeoPosition()
	 * @generated
	 */
	public static final int GEO_POSITION = 0;

	/**
	 * The feature id for the '<em><b>Longitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_POSITION__LONGITUDE = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Latitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_POSITION__LATITUDE = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Precision</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_POSITION__PRECISION = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Position</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_POSITION_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.CoordinateImpl <em>Coordinate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.CoordinateImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getCoordinate()
	 * @generated
	 */
	public static final int COORDINATE = 1;

	/**
	 * The feature id for the '<em><b>Decimal Degree</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int COORDINATE__DECIMAL_DEGREE = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Coordinate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int COORDINATE_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.LatitudeImpl <em>Latitude</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.LatitudeImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getLatitude()
	 * @generated
	 */
	public static final int LATITUDE = 2;

	/**
	 * The feature id for the '<em><b>Decimal Degree</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int LATITUDE__DECIMAL_DEGREE = COORDINATE__DECIMAL_DEGREE;

	/**
	 * The number of structural features of the '<em>Latitude</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int LATITUDE_FEATURE_COUNT = COORDINATE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.LongitudeImpl <em>Longitude</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.LongitudeImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getLongitude()
	 * @generated
	 */
	public static final int LONGITUDE = 3;

	/**
	 * The feature id for the '<em><b>Decimal Degree</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int LONGITUDE__DECIMAL_DEGREE = COORDINATE__DECIMAL_DEGREE;

	/**
	 * The number of structural features of the '<em>Longitude</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int LONGITUDE_FEATURE_COUNT = COORDINATE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.NamedArtifactImpl <em>Named Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.NamedArtifactImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getNamedArtifact()
	 * @generated
	 */
	public static final int NAMED_ARTIFACT = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAMED_ARTIFACT__NAME = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Named Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAMED_ARTIFACT_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.RouteImpl <em>Route</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.RouteImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getRoute()
	 * @generated
	 */
	public static final int ROUTE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ROUTE__NAME = NAMED_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Waypoints</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ROUTE__WAYPOINTS = NAMED_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Route</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ROUTE_FEATURE_COUNT = NAMED_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.NamedPositionImpl <em>Named Position</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.NamedPositionImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getNamedPosition()
	 * @generated
	 */
	public static final int NAMED_POSITION = 6;

	/**
	 * The feature id for the '<em><b>Longitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAMED_POSITION__LONGITUDE = GEO_POSITION__LONGITUDE;

	/**
	 * The feature id for the '<em><b>Latitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAMED_POSITION__LATITUDE = GEO_POSITION__LATITUDE;

	/**
	 * The feature id for the '<em><b>Precision</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAMED_POSITION__PRECISION = GEO_POSITION__PRECISION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAMED_POSITION__NAME = GEO_POSITION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Named Position</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAMED_POSITION_FEATURE_COUNT = GEO_POSITION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.RoutingContainerImpl <em>Routing Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.RoutingContainerImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getRoutingContainer()
	 * @generated
	 */
	public static final int ROUTING_CONTAINER = 7;

	/**
	 * The feature id for the '<em><b>Routes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ROUTING_CONTAINER__ROUTES = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Area</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ROUTING_CONTAINER__AREA = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Routing Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ROUTING_CONTAINER_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.POIContainerImpl <em>POI Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.POIContainerImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getPOIContainer()
	 * @generated
	 */
	public static final int POI_CONTAINER = 8;

	/**
	 * The feature id for the '<em><b>Pois</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int POI_CONTAINER__POIS = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Area</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int POI_CONTAINER__AREA = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>POI Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int POI_CONTAINER_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.ChartContainerImpl <em>Chart Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.ChartContainerImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getChartContainer()
	 * @generated
	 */
	public static final int CHART_CONTAINER = 9;

	/**
	 * The feature id for the '<em><b>Charts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CHART_CONTAINER__CHARTS = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Chart Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CHART_CONTAINER_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.NavigationCompoundImpl <em>Navigation Compound</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.NavigationCompoundImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getNavigationCompound()
	 * @generated
	 */
	public static final int NAVIGATION_COMPOUND = 10;

	/**
	 * The feature id for the '<em><b>Poi Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVIGATION_COMPOUND__POI_CONTAINER = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Routing Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVIGATION_COMPOUND__ROUTING_CONTAINER = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Tracks Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVIGATION_COMPOUND__TRACKS_CONTAINER = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Navigation Compound</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVIGATION_COMPOUND_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.ChartImpl <em>Chart</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.ChartImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getChart()
	 * @generated
	 */
	public static final int CHART = 11;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CHART__NAME = NAMED_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Chart Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CHART__CHART_CONFIGURATION = NAMED_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Chart</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CHART_FEATURE_COUNT = NAMED_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.GeoPosition3DImpl <em>Position3 D</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.GeoPosition3DImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getGeoPosition3D()
	 * @generated
	 */
	public static final int GEO_POSITION3_D = 12;

	/**
	 * The feature id for the '<em><b>Longitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_POSITION3_D__LONGITUDE = GEO_POSITION__LONGITUDE;

	/**
	 * The feature id for the '<em><b>Latitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_POSITION3_D__LATITUDE = GEO_POSITION__LATITUDE;

	/**
	 * The feature id for the '<em><b>Precision</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_POSITION3_D__PRECISION = GEO_POSITION__PRECISION;

	/**
	 * The feature id for the '<em><b>Altitude</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_POSITION3_D__ALTITUDE = GEO_POSITION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Position3 D</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_POSITION3_D_FEATURE_COUNT = GEO_POSITION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.MeasuredPosition3DImpl <em>Measured Position3 D</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.MeasuredPosition3DImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getMeasuredPosition3D()
	 * @generated
	 */
	public static final int MEASURED_POSITION3_D = 13;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASURED_POSITION3_D__SENSOR_ID = PhysxPackageImpl.MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASURED_POSITION3_D__TIME = PhysxPackageImpl.MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASURED_POSITION3_D__TIMEZONE = PhysxPackageImpl.MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASURED_POSITION3_D__VALID = PhysxPackageImpl.MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASURED_POSITION3_D__RELATIVE = PhysxPackageImpl.MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>Longitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASURED_POSITION3_D__LONGITUDE = PhysxPackageImpl.MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Latitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASURED_POSITION3_D__LATITUDE = PhysxPackageImpl.MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Precision</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASURED_POSITION3_D__PRECISION = PhysxPackageImpl.MEASUREMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Altitude</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASURED_POSITION3_D__ALTITUDE = PhysxPackageImpl.MEASUREMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Measured Position3 D</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASURED_POSITION3_D_FEATURE_COUNT = PhysxPackageImpl.MEASUREMENT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.TracksContainerImpl <em>Tracks Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.TracksContainerImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getTracksContainer()
	 * @generated
	 */
	public static final int TRACKS_CONTAINER = 14;

	/**
	 * The feature id for the '<em><b>Tracks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TRACKS_CONTAINER__TRACKS = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Tracks Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TRACKS_CONTAINER_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.TrackImpl <em>Track</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.TrackImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getTrack()
	 * @generated
	 */
	public static final int TRACK = 15;

	/**
	 * The feature id for the '<em><b>Measured Position</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TRACK__MEASURED_POSITION = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Track</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TRACK_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.ChartSymbolImpl <em>Chart Symbol</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.ChartSymbolImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getChartSymbol()
	 * @generated
	 */
	public static final int CHART_SYMBOL = 16;

	/**
	 * The number of structural features of the '<em>Chart Symbol</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CHART_SYMBOL_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.ChartAreaImpl <em>Chart Area</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.ChartAreaImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getChartArea()
	 * @generated
	 */
	public static final int CHART_AREA = 17;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CHART_AREA__BOUNDS = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Chart Area</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CHART_AREA_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.ChartWayImpl <em>Chart Way</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.ChartWayImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getChartWay()
	 * @generated
	 */
	public static final int CHART_WAY = 18;

	/**
	 * The feature id for the '<em><b>Waypoints</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CHART_WAY__WAYPOINTS = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Chart Way</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CHART_WAY_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.NavareaImpl <em>Navarea</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.NavareaImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getNavarea()
	 * @generated
	 */
	public static final int NAVAREA = 19;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVAREA__NAME = OsmPackageImpl.AREA__NAME;

	/**
	 * The feature id for the '<em><b>Chart Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVAREA__CHART_CONFIGURATION = OsmPackageImpl.AREA__CHART_CONFIGURATION;

	/**
	 * The feature id for the '<em><b>Poi Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVAREA__POI_CONTAINER = OsmPackageImpl.AREA__POI_CONTAINER;

	/**
	 * The feature id for the '<em><b>Routing Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVAREA__ROUTING_CONTAINER = OsmPackageImpl.AREA__ROUTING_CONTAINER;

	/**
	 * The feature id for the '<em><b>Tracks Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVAREA__TRACKS_CONTAINER = OsmPackageImpl.AREA__TRACKS_CONTAINER;

	/**
	 * The feature id for the '<em><b>Zoom Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVAREA__ZOOM_LEVEL = OsmPackageImpl.AREA__ZOOM_LEVEL;

	/**
	 * The feature id for the '<em><b>Map Center Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVAREA__MAP_CENTER_POSITION = OsmPackageImpl.AREA__MAP_CENTER_POSITION;

	/**
	 * The feature id for the '<em><b>Sub Area</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVAREA__SUB_AREA = OsmPackageImpl.AREA__SUB_AREA;

	/**
	 * The feature id for the '<em><b>Areanumber</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVAREA__AREANUMBER = OsmPackageImpl.AREA_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Navarea</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NAVAREA_FEATURE_COUNT = OsmPackageImpl.AREA_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.DepthImpl <em>Depth</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.DepthImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getDepth()
	 * @generated
	 */
	public static final int DEPTH = 20;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DEPTH__SENSOR_ID = PhysxPackageImpl.MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DEPTH__TIME = PhysxPackageImpl.MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DEPTH__TIMEZONE = PhysxPackageImpl.MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DEPTH__VALID = PhysxPackageImpl.MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DEPTH__RELATIVE = PhysxPackageImpl.MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>Measurement Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DEPTH__MEASUREMENT_POSITION = PhysxPackageImpl.MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DEPTH__DEPTH = PhysxPackageImpl.MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Depth</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DEPTH_FEATURE_COUNT = PhysxPackageImpl.MEASUREMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.GNSSMeasuredPositionImpl <em>GNSS Measured Position</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.GNSSMeasuredPositionImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getGNSSMeasuredPosition()
	 * @generated
	 */
	public static final int GNSS_MEASURED_POSITION = 21;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__SENSOR_ID = MEASURED_POSITION3_D__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__TIME = MEASURED_POSITION3_D__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__TIMEZONE = MEASURED_POSITION3_D__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__VALID = MEASURED_POSITION3_D__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__RELATIVE = MEASURED_POSITION3_D__RELATIVE;

	/**
	 * The feature id for the '<em><b>Longitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__LONGITUDE = MEASURED_POSITION3_D__LONGITUDE;

	/**
	 * The feature id for the '<em><b>Latitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__LATITUDE = MEASURED_POSITION3_D__LATITUDE;

	/**
	 * The feature id for the '<em><b>Precision</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__PRECISION = MEASURED_POSITION3_D__PRECISION;

	/**
	 * The feature id for the '<em><b>Altitude</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__ALTITUDE = MEASURED_POSITION3_D__ALTITUDE;

	/**
	 * The feature id for the '<em><b>Hdop</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__HDOP = MEASURED_POSITION3_D_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vdop</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__VDOP = MEASURED_POSITION3_D_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Pdop</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__PDOP = MEASURED_POSITION3_D_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Augmentation</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION__AUGMENTATION = MEASURED_POSITION3_D_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>GNSS Measured Position</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GNSS_MEASURED_POSITION_FEATURE_COUNT = MEASURED_POSITION3_D_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.AnchorPositionImpl <em>Anchor Position</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.AnchorPositionImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getAnchorPosition()
	 * @generated
	 */
	public static final int ANCHOR_POSITION = 22;

	/**
	 * The feature id for the '<em><b>Longitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ANCHOR_POSITION__LONGITUDE = GEO_POSITION__LONGITUDE;

	/**
	 * The feature id for the '<em><b>Latitude</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ANCHOR_POSITION__LATITUDE = GEO_POSITION__LATITUDE;

	/**
	 * The feature id for the '<em><b>Precision</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ANCHOR_POSITION__PRECISION = GEO_POSITION__PRECISION;

	/**
	 * The feature id for the '<em><b>XExtent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ANCHOR_POSITION__XEXTENT = GEO_POSITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>YExtent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ANCHOR_POSITION__YEXTENT = GEO_POSITION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Anchor Position</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ANCHOR_POSITION_FEATURE_COUNT = GEO_POSITION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.impl.GeoBoundingBoxImpl <em>Bounding Box</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.impl.GeoBoundingBoxImpl
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getGeoBoundingBox()
	 * @generated
	 */
	public static final int GEO_BOUNDING_BOX = 23;

	/**
	 * The feature id for the '<em><b>Top</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_BOUNDING_BOX__TOP = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Bottom</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_BOUNDING_BOX__BOTTOM = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Left</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_BOUNDING_BOX__LEFT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Right</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_BOUNDING_BOX__RIGHT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Bounding Box</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int GEO_BOUNDING_BOX_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.Direction <em>Direction</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.Direction
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getDirection()
	 * @generated
	 */
	public static final int DIRECTION = 24;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.LatitudeHemisphere <em>Latitude Hemisphere</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.LatitudeHemisphere
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getLatitudeHemisphere()
	 * @generated
	 */
	public static final int LATITUDE_HEMISPHERE = 25;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.LongitudeHemisphere <em>Longitude Hemisphere</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.LongitudeHemisphere
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getLongitudeHemisphere()
	 * @generated
	 */
	public static final int LONGITUDE_HEMISPHERE = 26;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.RelativeDepthMeasurementPosition <em>Relative Depth Measurement Position</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.RelativeDepthMeasurementPosition
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getRelativeDepthMeasurementPosition()
	 * @generated
	 */
	public static final int RELATIVE_DEPTH_MEASUREMENT_POSITION = 27;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass geoPositionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass coordinateEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass latitudeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass longitudeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass routeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedArtifactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedPositionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass routingContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass poiContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass navareaEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass depthEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass gnssMeasuredPositionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass anchorPositionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass geoBoundingBoxEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum directionEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum latitudeHemisphereEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum longitudeHemisphereEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum relativeDepthMeasurementPositionEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass chartContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass navigationCompoundEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass chartEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass geoPosition3DEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass measuredPosition3DEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tracksContainerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass trackEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass chartSymbolEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass chartAreaEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass chartWayEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private GeoPackageImpl() {
		super(eNS_URI, ((EFactory)GeoFactory.INSTANCE));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link GeoPackageImpl#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static GeoPackageImpl init() {
		if (isInited) return (GeoPackageImpl)EPackage.Registry.INSTANCE.getEPackage(GeoPackageImpl.eNS_URI);

		// Obtain or create and register package
		GeoPackageImpl theGeoPackage = (GeoPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof GeoPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new GeoPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		CorePackageImpl theCorePackage = (CorePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI) instanceof CorePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI) : CorePackageImpl.eINSTANCE);
		OsmPackageImpl theOsmPackage = (OsmPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(OsmPackageImpl.eNS_URI) instanceof OsmPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OsmPackageImpl.eNS_URI) : OsmPackageImpl.eINSTANCE);
		PhysxPackageImpl thePhysxPackage = (PhysxPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PhysxPackageImpl.eNS_URI) instanceof PhysxPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PhysxPackageImpl.eNS_URI) : PhysxPackageImpl.eINSTANCE);
		WeatherPackageImpl theWeatherPackage = (WeatherPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(WeatherPackageImpl.eNS_URI) instanceof WeatherPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(WeatherPackageImpl.eNS_URI) : WeatherPackageImpl.eINSTANCE);
		DataPackageImpl theDataPackage = (DataPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DataPackageImpl.eNS_URI) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DataPackageImpl.eNS_URI) : DataPackageImpl.eINSTANCE);
		DiagramInterchangePackageImpl theDiagramInterchangePackage = (DiagramInterchangePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackageImpl.eNS_URI) instanceof DiagramInterchangePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackageImpl.eNS_URI) : DiagramInterchangePackageImpl.eINSTANCE);

		// Create package meta-data objects
		theGeoPackage.createPackageContents();
		theCorePackage.createPackageContents();
		theOsmPackage.createPackageContents();
		thePhysxPackage.createPackageContents();
		theWeatherPackage.createPackageContents();
		theDataPackage.createPackageContents();
		theDiagramInterchangePackage.createPackageContents();

		// Initialize created meta-data
		theGeoPackage.initializePackageContents();
		theCorePackage.initializePackageContents();
		theOsmPackage.initializePackageContents();
		thePhysxPackage.initializePackageContents();
		theWeatherPackage.initializePackageContents();
		theDataPackage.initializePackageContents();
		theDiagramInterchangePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theGeoPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(GeoPackageImpl.eNS_URI, theGeoPackage);
		return theGeoPackage;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.GeoPosition <em>Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Position</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoPosition
	 * @generated
	 */
	public EClass getGeoPosition() {
		return geoPositionEClass;
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.geo.GeoPosition#getLongitude <em>Longitude</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Longitude</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoPosition#getLongitude()
	 * @see #getGeoPosition()
	 * @generated
	 */
	public EReference getGeoPosition_Longitude() {
		return (EReference)geoPositionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.geo.GeoPosition#getLatitude <em>Latitude</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Latitude</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoPosition#getLatitude()
	 * @see #getGeoPosition()
	 * @generated
	 */
	public EReference getGeoPosition_Latitude() {
		return (EReference)geoPositionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.GeoPosition#getPrecision <em>Precision</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Precision</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoPosition#getPrecision()
	 * @see #getGeoPosition()
	 * @generated
	 */
	public EAttribute getGeoPosition_Precision() {
		return (EAttribute)geoPositionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.Coordinate <em>Coordinate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coordinate</em>'.
	 * @see net.sf.seesea.model.core.geo.Coordinate
	 * @generated
	 */
	public EClass getCoordinate() {
		return coordinateEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.Coordinate#getDecimalDegree <em>Decimal Degree</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Decimal Degree</em>'.
	 * @see net.sf.seesea.model.core.geo.Coordinate#getDecimalDegree()
	 * @see #getCoordinate()
	 * @generated
	 */
	public EAttribute getCoordinate_DecimalDegree() {
		return (EAttribute)coordinateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.Latitude <em>Latitude</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Latitude</em>'.
	 * @see net.sf.seesea.model.core.geo.Latitude
	 * @generated
	 */
	public EClass getLatitude() {
		return latitudeEClass;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.Longitude <em>Longitude</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Longitude</em>'.
	 * @see net.sf.seesea.model.core.geo.Longitude
	 * @generated
	 */
	public EClass getLongitude() {
		return longitudeEClass;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.Route <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Route</em>'.
	 * @see net.sf.seesea.model.core.geo.Route
	 * @generated
	 */
	public EClass getRoute() {
		return routeEClass;
	}

	/**
	 * Returns the meta object for the reference list '{@link net.sf.seesea.model.core.geo.Route#getWaypoints <em>Waypoints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Waypoints</em>'.
	 * @see net.sf.seesea.model.core.geo.Route#getWaypoints()
	 * @see #getRoute()
	 * @generated
	 */
	public EReference getRoute_Waypoints() {
		return (EReference)routeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.NamedArtifact <em>Named Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Artifact</em>'.
	 * @see net.sf.seesea.model.core.geo.NamedArtifact
	 * @generated
	 */
	public EClass getNamedArtifact() {
		return namedArtifactEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.NamedArtifact#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.sf.seesea.model.core.geo.NamedArtifact#getName()
	 * @see #getNamedArtifact()
	 * @generated
	 */
	public EAttribute getNamedArtifact_Name() {
		return (EAttribute)namedArtifactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.NamedPosition <em>Named Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Position</em>'.
	 * @see net.sf.seesea.model.core.geo.NamedPosition
	 * @generated
	 */
	public EClass getNamedPosition() {
		return namedPositionEClass;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.RoutingContainer <em>Routing Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Routing Container</em>'.
	 * @see net.sf.seesea.model.core.geo.RoutingContainer
	 * @generated
	 */
	public EClass getRoutingContainer() {
		return routingContainerEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link net.sf.seesea.model.core.geo.RoutingContainer#getRoutes <em>Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Routes</em>'.
	 * @see net.sf.seesea.model.core.geo.RoutingContainer#getRoutes()
	 * @see #getRoutingContainer()
	 * @generated
	 */
	public EReference getRoutingContainer_Routes() {
		return (EReference)routingContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the reference '{@link net.sf.seesea.model.core.geo.RoutingContainer#getArea <em>Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Area</em>'.
	 * @see net.sf.seesea.model.core.geo.RoutingContainer#getArea()
	 * @see #getRoutingContainer()
	 * @generated
	 */
	public EReference getRoutingContainer_Area() {
		return (EReference)routingContainerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.POIContainer <em>POI Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>POI Container</em>'.
	 * @see net.sf.seesea.model.core.geo.POIContainer
	 * @generated
	 */
	public EClass getPOIContainer() {
		return poiContainerEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link net.sf.seesea.model.core.geo.POIContainer#getPois <em>Pois</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Pois</em>'.
	 * @see net.sf.seesea.model.core.geo.POIContainer#getPois()
	 * @see #getPOIContainer()
	 * @generated
	 */
	public EReference getPOIContainer_Pois() {
		return (EReference)poiContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the reference '{@link net.sf.seesea.model.core.geo.POIContainer#getArea <em>Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Area</em>'.
	 * @see net.sf.seesea.model.core.geo.POIContainer#getArea()
	 * @see #getPOIContainer()
	 * @generated
	 */
	public EReference getPOIContainer_Area() {
		return (EReference)poiContainerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.Navarea <em>Navarea</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Navarea</em>'.
	 * @see net.sf.seesea.model.core.geo.Navarea
	 * @generated
	 */
	public EClass getNavarea() {
		return navareaEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.Navarea#getAreanumber <em>Areanumber</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Areanumber</em>'.
	 * @see net.sf.seesea.model.core.geo.Navarea#getAreanumber()
	 * @see #getNavarea()
	 * @generated
	 */
	public EAttribute getNavarea_Areanumber() {
		return (EAttribute)navareaEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.Depth <em>Depth</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Depth</em>'.
	 * @see net.sf.seesea.model.core.geo.Depth
	 * @generated
	 */
	public EClass getDepth() {
		return depthEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.Depth#getMeasurementPosition <em>Measurement Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Measurement Position</em>'.
	 * @see net.sf.seesea.model.core.geo.Depth#getMeasurementPosition()
	 * @see #getDepth()
	 * @generated
	 */
	public EAttribute getDepth_MeasurementPosition() {
		return (EAttribute)depthEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.Depth#getDepth <em>Depth</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Depth</em>'.
	 * @see net.sf.seesea.model.core.geo.Depth#getDepth()
	 * @see #getDepth()
	 * @generated
	 */
	public EAttribute getDepth_Depth() {
		return (EAttribute)depthEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.GNSSMeasuredPosition <em>GNSS Measured Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>GNSS Measured Position</em>'.
	 * @see net.sf.seesea.model.core.geo.GNSSMeasuredPosition
	 * @generated
	 */
	public EClass getGNSSMeasuredPosition() {
		return gnssMeasuredPositionEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.GNSSMeasuredPosition#getHdop <em>Hdop</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hdop</em>'.
	 * @see net.sf.seesea.model.core.geo.GNSSMeasuredPosition#getHdop()
	 * @see #getGNSSMeasuredPosition()
	 * @generated
	 */
	public EAttribute getGNSSMeasuredPosition_Hdop() {
		return (EAttribute)gnssMeasuredPositionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.GNSSMeasuredPosition#getVdop <em>Vdop</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vdop</em>'.
	 * @see net.sf.seesea.model.core.geo.GNSSMeasuredPosition#getVdop()
	 * @see #getGNSSMeasuredPosition()
	 * @generated
	 */
	public EAttribute getGNSSMeasuredPosition_Vdop() {
		return (EAttribute)gnssMeasuredPositionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.GNSSMeasuredPosition#getPdop <em>Pdop</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pdop</em>'.
	 * @see net.sf.seesea.model.core.geo.GNSSMeasuredPosition#getPdop()
	 * @see #getGNSSMeasuredPosition()
	 * @generated
	 */
	public EAttribute getGNSSMeasuredPosition_Pdop() {
		return (EAttribute)gnssMeasuredPositionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for the attribute list '{@link net.sf.seesea.model.core.geo.GNSSMeasuredPosition#getAugmentation <em>Augmentation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Augmentation</em>'.
	 * @see net.sf.seesea.model.core.geo.GNSSMeasuredPosition#getAugmentation()
	 * @see #getGNSSMeasuredPosition()
	 * @generated
	 */
	public EAttribute getGNSSMeasuredPosition_Augmentation() {
		return (EAttribute)gnssMeasuredPositionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.AnchorPosition <em>Anchor Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Anchor Position</em>'.
	 * @see net.sf.seesea.model.core.geo.AnchorPosition
	 * @generated
	 */
	public EClass getAnchorPosition() {
		return anchorPositionEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.AnchorPosition#getXExtent <em>XExtent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>XExtent</em>'.
	 * @see net.sf.seesea.model.core.geo.AnchorPosition#getXExtent()
	 * @see #getAnchorPosition()
	 * @generated
	 */
	public EAttribute getAnchorPosition_XExtent() {
		return (EAttribute)anchorPositionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.AnchorPosition#getYExtent <em>YExtent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>YExtent</em>'.
	 * @see net.sf.seesea.model.core.geo.AnchorPosition#getYExtent()
	 * @see #getAnchorPosition()
	 * @generated
	 */
	public EAttribute getAnchorPosition_YExtent() {
		return (EAttribute)anchorPositionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.GeoBoundingBox <em>Bounding Box</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bounding Box</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoBoundingBox
	 * @generated
	 */
	public EClass getGeoBoundingBox() {
		return geoBoundingBoxEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.GeoBoundingBox#getTop <em>Top</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Top</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoBoundingBox#getTop()
	 * @see #getGeoBoundingBox()
	 * @generated
	 */
	public EAttribute getGeoBoundingBox_Top() {
		return (EAttribute)geoBoundingBoxEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.GeoBoundingBox#getBottom <em>Bottom</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bottom</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoBoundingBox#getBottom()
	 * @see #getGeoBoundingBox()
	 * @generated
	 */
	public EAttribute getGeoBoundingBox_Bottom() {
		return (EAttribute)geoBoundingBoxEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.GeoBoundingBox#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Left</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoBoundingBox#getLeft()
	 * @see #getGeoBoundingBox()
	 * @generated
	 */
	public EAttribute getGeoBoundingBox_Left() {
		return (EAttribute)geoBoundingBoxEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.GeoBoundingBox#getRight <em>Right</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Right</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoBoundingBox#getRight()
	 * @see #getGeoBoundingBox()
	 * @generated
	 */
	public EAttribute getGeoBoundingBox_Right() {
		return (EAttribute)geoBoundingBoxEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.geo.Direction <em>Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Direction</em>'.
	 * @see net.sf.seesea.model.core.geo.Direction
	 * @generated
	 */
	public EEnum getDirection() {
		return directionEEnum;
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.geo.LatitudeHemisphere <em>Latitude Hemisphere</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Latitude Hemisphere</em>'.
	 * @see net.sf.seesea.model.core.geo.LatitudeHemisphere
	 * @generated
	 */
	public EEnum getLatitudeHemisphere() {
		return latitudeHemisphereEEnum;
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.geo.LongitudeHemisphere <em>Longitude Hemisphere</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Longitude Hemisphere</em>'.
	 * @see net.sf.seesea.model.core.geo.LongitudeHemisphere
	 * @generated
	 */
	public EEnum getLongitudeHemisphere() {
		return longitudeHemisphereEEnum;
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.geo.RelativeDepthMeasurementPosition <em>Relative Depth Measurement Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Relative Depth Measurement Position</em>'.
	 * @see net.sf.seesea.model.core.geo.RelativeDepthMeasurementPosition
	 * @generated
	 */
	public EEnum getRelativeDepthMeasurementPosition() {
		return relativeDepthMeasurementPositionEEnum;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.ChartContainer <em>Chart Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Chart Container</em>'.
	 * @see net.sf.seesea.model.core.geo.ChartContainer
	 * @generated
	 */
	public EClass getChartContainer() {
		return chartContainerEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link net.sf.seesea.model.core.geo.ChartContainer#getCharts <em>Charts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Charts</em>'.
	 * @see net.sf.seesea.model.core.geo.ChartContainer#getCharts()
	 * @see #getChartContainer()
	 * @generated
	 */
	public EReference getChartContainer_Charts() {
		return (EReference)chartContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.NavigationCompound <em>Navigation Compound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Navigation Compound</em>'.
	 * @see net.sf.seesea.model.core.geo.NavigationCompound
	 * @generated
	 */
	public EClass getNavigationCompound() {
		return navigationCompoundEClass;
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.geo.NavigationCompound#getPoiContainer <em>Poi Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Poi Container</em>'.
	 * @see net.sf.seesea.model.core.geo.NavigationCompound#getPoiContainer()
	 * @see #getNavigationCompound()
	 * @generated
	 */
	public EReference getNavigationCompound_PoiContainer() {
		return (EReference)navigationCompoundEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.geo.NavigationCompound#getRoutingContainer <em>Routing Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Routing Container</em>'.
	 * @see net.sf.seesea.model.core.geo.NavigationCompound#getRoutingContainer()
	 * @see #getNavigationCompound()
	 * @generated
	 */
	public EReference getNavigationCompound_RoutingContainer() {
		return (EReference)navigationCompoundEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.geo.NavigationCompound#getTracksContainer <em>Tracks Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Tracks Container</em>'.
	 * @see net.sf.seesea.model.core.geo.NavigationCompound#getTracksContainer()
	 * @see #getNavigationCompound()
	 * @generated
	 */
	public EReference getNavigationCompound_TracksContainer() {
		return (EReference)navigationCompoundEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.Chart <em>Chart</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Chart</em>'.
	 * @see net.sf.seesea.model.core.geo.Chart
	 * @generated
	 */
	public EClass getChart() {
		return chartEClass;
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.geo.Chart#getChartConfiguration <em>Chart Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Chart Configuration</em>'.
	 * @see net.sf.seesea.model.core.geo.Chart#getChartConfiguration()
	 * @see #getChart()
	 * @generated
	 */
	public EReference getChart_ChartConfiguration() {
		return (EReference)chartEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.GeoPosition3D <em>Position3 D</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Position3 D</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoPosition3D
	 * @generated
	 */
	public EClass getGeoPosition3D() {
		return geoPosition3DEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.GeoPosition3D#getAltitude <em>Altitude</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Altitude</em>'.
	 * @see net.sf.seesea.model.core.geo.GeoPosition3D#getAltitude()
	 * @see #getGeoPosition3D()
	 * @generated
	 */
	public EAttribute getGeoPosition3D_Altitude() {
		return (EAttribute)geoPosition3DEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.MeasuredPosition3D <em>Measured Position3 D</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Measured Position3 D</em>'.
	 * @see net.sf.seesea.model.core.geo.MeasuredPosition3D
	 * @generated
	 */
	public EClass getMeasuredPosition3D() {
		return measuredPosition3DEClass;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.TracksContainer <em>Tracks Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tracks Container</em>'.
	 * @see net.sf.seesea.model.core.geo.TracksContainer
	 * @generated
	 */
	public EClass getTracksContainer() {
		return tracksContainerEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link net.sf.seesea.model.core.geo.TracksContainer#getTracks <em>Tracks</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tracks</em>'.
	 * @see net.sf.seesea.model.core.geo.TracksContainer#getTracks()
	 * @see #getTracksContainer()
	 * @generated
	 */
	public EReference getTracksContainer_Tracks() {
		return (EReference)tracksContainerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.Track <em>Track</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Track</em>'.
	 * @see net.sf.seesea.model.core.geo.Track
	 * @generated
	 */
	public EClass getTrack() {
		return trackEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link net.sf.seesea.model.core.geo.Track#getMeasuredPosition <em>Measured Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Measured Position</em>'.
	 * @see net.sf.seesea.model.core.geo.Track#getMeasuredPosition()
	 * @see #getTrack()
	 * @generated
	 */
	public EReference getTrack_MeasuredPosition() {
		return (EReference)trackEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.ChartSymbol <em>Chart Symbol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Chart Symbol</em>'.
	 * @see net.sf.seesea.model.core.geo.ChartSymbol
	 * @generated
	 */
	public EClass getChartSymbol() {
		return chartSymbolEClass;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.ChartArea <em>Chart Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Chart Area</em>'.
	 * @see net.sf.seesea.model.core.geo.ChartArea
	 * @generated
	 */
	public EClass getChartArea() {
		return chartAreaEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link net.sf.seesea.model.core.geo.ChartArea#getBounds <em>Bounds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Bounds</em>'.
	 * @see net.sf.seesea.model.core.geo.ChartArea#getBounds()
	 * @see #getChartArea()
	 * @generated
	 */
	public EReference getChartArea_Bounds() {
		return (EReference)chartAreaEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.ChartWay <em>Chart Way</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Chart Way</em>'.
	 * @see net.sf.seesea.model.core.geo.ChartWay
	 * @generated
	 */
	public EClass getChartWay() {
		return chartWayEClass;
	}

	/**
	 * Returns the meta object for the reference list '{@link net.sf.seesea.model.core.geo.ChartWay#getWaypoints <em>Waypoints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Waypoints</em>'.
	 * @see net.sf.seesea.model.core.geo.ChartWay#getWaypoints()
	 * @see #getChartWay()
	 * @generated
	 */
	public EReference getChartWay_Waypoints() {
		return (EReference)chartWayEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	public GeoFactory getGeoFactory() {
		return (GeoFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		geoPositionEClass = createEClass(GEO_POSITION);
		createEReference(geoPositionEClass, GEO_POSITION__LONGITUDE);
		createEReference(geoPositionEClass, GEO_POSITION__LATITUDE);
		createEAttribute(geoPositionEClass, GEO_POSITION__PRECISION);

		coordinateEClass = createEClass(COORDINATE);
		createEAttribute(coordinateEClass, COORDINATE__DECIMAL_DEGREE);

		latitudeEClass = createEClass(LATITUDE);

		longitudeEClass = createEClass(LONGITUDE);

		routeEClass = createEClass(ROUTE);
		createEReference(routeEClass, ROUTE__WAYPOINTS);

		namedArtifactEClass = createEClass(NAMED_ARTIFACT);
		createEAttribute(namedArtifactEClass, NAMED_ARTIFACT__NAME);

		namedPositionEClass = createEClass(NAMED_POSITION);

		routingContainerEClass = createEClass(ROUTING_CONTAINER);
		createEReference(routingContainerEClass, ROUTING_CONTAINER__ROUTES);
		createEReference(routingContainerEClass, ROUTING_CONTAINER__AREA);

		poiContainerEClass = createEClass(POI_CONTAINER);
		createEReference(poiContainerEClass, POI_CONTAINER__POIS);
		createEReference(poiContainerEClass, POI_CONTAINER__AREA);

		chartContainerEClass = createEClass(CHART_CONTAINER);
		createEReference(chartContainerEClass, CHART_CONTAINER__CHARTS);

		navigationCompoundEClass = createEClass(NAVIGATION_COMPOUND);
		createEReference(navigationCompoundEClass, NAVIGATION_COMPOUND__POI_CONTAINER);
		createEReference(navigationCompoundEClass, NAVIGATION_COMPOUND__ROUTING_CONTAINER);
		createEReference(navigationCompoundEClass, NAVIGATION_COMPOUND__TRACKS_CONTAINER);

		chartEClass = createEClass(CHART);
		createEReference(chartEClass, CHART__CHART_CONFIGURATION);

		geoPosition3DEClass = createEClass(GEO_POSITION3_D);
		createEAttribute(geoPosition3DEClass, GEO_POSITION3_D__ALTITUDE);

		measuredPosition3DEClass = createEClass(MEASURED_POSITION3_D);

		tracksContainerEClass = createEClass(TRACKS_CONTAINER);
		createEReference(tracksContainerEClass, TRACKS_CONTAINER__TRACKS);

		trackEClass = createEClass(TRACK);
		createEReference(trackEClass, TRACK__MEASURED_POSITION);

		chartSymbolEClass = createEClass(CHART_SYMBOL);

		chartAreaEClass = createEClass(CHART_AREA);
		createEReference(chartAreaEClass, CHART_AREA__BOUNDS);

		chartWayEClass = createEClass(CHART_WAY);
		createEReference(chartWayEClass, CHART_WAY__WAYPOINTS);

		navareaEClass = createEClass(NAVAREA);
		createEAttribute(navareaEClass, NAVAREA__AREANUMBER);

		depthEClass = createEClass(DEPTH);
		createEAttribute(depthEClass, DEPTH__MEASUREMENT_POSITION);
		createEAttribute(depthEClass, DEPTH__DEPTH);

		gnssMeasuredPositionEClass = createEClass(GNSS_MEASURED_POSITION);
		createEAttribute(gnssMeasuredPositionEClass, GNSS_MEASURED_POSITION__HDOP);
		createEAttribute(gnssMeasuredPositionEClass, GNSS_MEASURED_POSITION__VDOP);
		createEAttribute(gnssMeasuredPositionEClass, GNSS_MEASURED_POSITION__PDOP);
		createEAttribute(gnssMeasuredPositionEClass, GNSS_MEASURED_POSITION__AUGMENTATION);

		anchorPositionEClass = createEClass(ANCHOR_POSITION);
		createEAttribute(anchorPositionEClass, ANCHOR_POSITION__XEXTENT);
		createEAttribute(anchorPositionEClass, ANCHOR_POSITION__YEXTENT);

		geoBoundingBoxEClass = createEClass(GEO_BOUNDING_BOX);
		createEAttribute(geoBoundingBoxEClass, GEO_BOUNDING_BOX__TOP);
		createEAttribute(geoBoundingBoxEClass, GEO_BOUNDING_BOX__BOTTOM);
		createEAttribute(geoBoundingBoxEClass, GEO_BOUNDING_BOX__LEFT);
		createEAttribute(geoBoundingBoxEClass, GEO_BOUNDING_BOX__RIGHT);

		// Create enums
		directionEEnum = createEEnum(DIRECTION);
		latitudeHemisphereEEnum = createEEnum(LATITUDE_HEMISPHERE);
		longitudeHemisphereEEnum = createEEnum(LONGITUDE_HEMISPHERE);
		relativeDepthMeasurementPositionEEnum = createEEnum(RELATIVE_DEPTH_MEASUREMENT_POSITION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		OsmPackageImpl theOsmPackage = (OsmPackageImpl)EPackage.Registry.INSTANCE.getEPackage(OsmPackageImpl.eNS_URI);
		CorePackageImpl theCorePackage = (CorePackageImpl)EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI);
		DiagramInterchangePackageImpl theDiagramInterchangePackage = (DiagramInterchangePackageImpl)EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackageImpl.eNS_URI);
		PhysxPackageImpl thePhysxPackage = (PhysxPackageImpl)EPackage.Registry.INSTANCE.getEPackage(PhysxPackageImpl.eNS_URI);

		// Add subpackages
		getESubpackages().add(theOsmPackage);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		geoPositionEClass.getESuperTypes().add(theCorePackage.getModelObject());
		coordinateEClass.getESuperTypes().add(theCorePackage.getModelObject());
		latitudeEClass.getESuperTypes().add(this.getCoordinate());
		longitudeEClass.getESuperTypes().add(this.getCoordinate());
		routeEClass.getESuperTypes().add(this.getNamedArtifact());
		namedArtifactEClass.getESuperTypes().add(theCorePackage.getModelObject());
		namedPositionEClass.getESuperTypes().add(this.getGeoPosition());
		namedPositionEClass.getESuperTypes().add(this.getNamedArtifact());
		routingContainerEClass.getESuperTypes().add(theCorePackage.getModelObject());
		poiContainerEClass.getESuperTypes().add(theCorePackage.getModelObject());
		chartContainerEClass.getESuperTypes().add(theCorePackage.getModelObject());
		navigationCompoundEClass.getESuperTypes().add(theCorePackage.getModelObject());
		chartEClass.getESuperTypes().add(this.getNamedArtifact());
		geoPosition3DEClass.getESuperTypes().add(this.getGeoPosition());
		measuredPosition3DEClass.getESuperTypes().add(thePhysxPackage.getMeasurement());
		measuredPosition3DEClass.getESuperTypes().add(this.getGeoPosition3D());
		tracksContainerEClass.getESuperTypes().add(theCorePackage.getModelObject());
		trackEClass.getESuperTypes().add(theCorePackage.getModelObject());
		chartAreaEClass.getESuperTypes().add(theCorePackage.getModelObject());
		chartWayEClass.getESuperTypes().add(theCorePackage.getModelObject());
		navareaEClass.getESuperTypes().add(theOsmPackage.getArea());
		depthEClass.getESuperTypes().add(thePhysxPackage.getMeasurement());
		gnssMeasuredPositionEClass.getESuperTypes().add(this.getMeasuredPosition3D());
		anchorPositionEClass.getESuperTypes().add(this.getGeoPosition());
		geoBoundingBoxEClass.getESuperTypes().add(theCorePackage.getModelObject());

		// Initialize classes and features; add operations and parameters
		initEClass(geoPositionEClass, GeoPosition.class, "GeoPosition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getGeoPosition_Longitude(), this.getLongitude(), null, "longitude", null, 0, 1, GeoPosition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getGeoPosition_Latitude(), this.getLatitude(), null, "latitude", null, 0, 1, GeoPosition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getGeoPosition_Precision(), ecorePackage.getEInt(), "precision", null, 0, 1, GeoPosition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(coordinateEClass, Coordinate.class, "Coordinate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getCoordinate_DecimalDegree(), ecorePackage.getEDouble(), "decimalDegree", "0", 0, 1, Coordinate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

		addEOperation(coordinateEClass, ecorePackage.getEInt(), "getDegree", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(coordinateEClass, ecorePackage.getEInt(), "getMinute", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		addEOperation(coordinateEClass, ecorePackage.getEDouble(), "getSecond", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		EOperation op = addEOperation(coordinateEClass, null, "setDegree", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, ecorePackage.getEInt(), "degree", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op = addEOperation(coordinateEClass, null, "setMinute", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, ecorePackage.getEInt(), "minute", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		op = addEOperation(coordinateEClass, null, "setSecond", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
		addEParameter(op, ecorePackage.getEDouble(), "seconds", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

		initEClass(latitudeEClass, Latitude.class, "Latitude", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(longitudeEClass, Longitude.class, "Longitude", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(routeEClass, Route.class, "Route", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getRoute_Waypoints(), this.getNamedPosition(), null, "waypoints", null, 0, -1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(namedArtifactEClass, NamedArtifact.class, "NamedArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getNamedArtifact_Name(), ecorePackage.getEString(), "name", null, 0, 1, NamedArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(namedPositionEClass, NamedPosition.class, "NamedPosition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(routingContainerEClass, RoutingContainer.class, "RoutingContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getRoutingContainer_Routes(), this.getRoute(), null, "routes", null, 0, -1, RoutingContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getRoutingContainer_Area(), theOsmPackage.getArea(), null, "area", null, 0, 1, RoutingContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(poiContainerEClass, POIContainer.class, "POIContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getPOIContainer_Pois(), this.getNamedPosition(), null, "pois", null, 0, -1, POIContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getPOIContainer_Area(), theOsmPackage.getArea(), null, "area", null, 0, 1, POIContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(chartContainerEClass, ChartContainer.class, "ChartContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getChartContainer_Charts(), this.getChart(), null, "charts", null, 0, -1, ChartContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(navigationCompoundEClass, NavigationCompound.class, "NavigationCompound", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getNavigationCompound_PoiContainer(), this.getPOIContainer(), null, "poiContainer", null, 0, 1, NavigationCompound.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNavigationCompound_RoutingContainer(), this.getRoutingContainer(), null, "routingContainer", null, 0, 1, NavigationCompound.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getNavigationCompound_TracksContainer(), this.getTracksContainer(), null, "tracksContainer", null, 0, 1, NavigationCompound.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(chartEClass, Chart.class, "Chart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getChart_ChartConfiguration(), theDiagramInterchangePackage.getDiagram(), null, "chartConfiguration", null, 0, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(geoPosition3DEClass, GeoPosition3D.class, "GeoPosition3D", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getGeoPosition3D_Altitude(), ecorePackage.getEDouble(), "altitude", null, 0, 1, GeoPosition3D.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(measuredPosition3DEClass, MeasuredPosition3D.class, "MeasuredPosition3D", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(tracksContainerEClass, TracksContainer.class, "TracksContainer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getTracksContainer_Tracks(), this.getTrack(), null, "tracks", null, 0, -1, TracksContainer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(trackEClass, Track.class, "Track", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getTrack_MeasuredPosition(), this.getMeasuredPosition3D(), null, "measuredPosition", null, 0, -1, Track.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(chartSymbolEClass, ChartSymbol.class, "ChartSymbol", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(chartAreaEClass, ChartArea.class, "ChartArea", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getChartArea_Bounds(), this.getGeoPosition(), null, "bounds", null, 0, -1, ChartArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(chartWayEClass, ChartWay.class, "ChartWay", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getChartWay_Waypoints(), this.getGeoPosition(), null, "waypoints", null, 0, -1, ChartWay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(navareaEClass, Navarea.class, "Navarea", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getNavarea_Areanumber(), ecorePackage.getEInt(), "areanumber", null, 0, 1, Navarea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(depthEClass, Depth.class, "Depth", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getDepth_MeasurementPosition(), this.getRelativeDepthMeasurementPosition(), "measurementPosition", null, 0, 1, Depth.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getDepth_Depth(), ecorePackage.getEDouble(), "depth", null, 0, 1, Depth.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(gnssMeasuredPositionEClass, GNSSMeasuredPosition.class, "GNSSMeasuredPosition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getGNSSMeasuredPosition_Hdop(), ecorePackage.getEDouble(), "hdop", null, 0, 1, GNSSMeasuredPosition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getGNSSMeasuredPosition_Vdop(), ecorePackage.getEDouble(), "vdop", null, 0, 1, GNSSMeasuredPosition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getGNSSMeasuredPosition_Pdop(), ecorePackage.getEDouble(), "pdop", null, 0, 1, GNSSMeasuredPosition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getGNSSMeasuredPosition_Augmentation(), ecorePackage.getEString(), "augmentation", null, 0, -1, GNSSMeasuredPosition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(anchorPositionEClass, AnchorPosition.class, "AnchorPosition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getAnchorPosition_XExtent(), ecorePackage.getEDouble(), "xExtent", null, 0, 1, AnchorPosition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getAnchorPosition_YExtent(), ecorePackage.getEDouble(), "yExtent", null, 0, 1, AnchorPosition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(geoBoundingBoxEClass, GeoBoundingBox.class, "GeoBoundingBox", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getGeoBoundingBox_Top(), ecorePackage.getEDouble(), "top", null, 0, 1, GeoBoundingBox.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getGeoBoundingBox_Bottom(), ecorePackage.getEDouble(), "bottom", null, 0, 1, GeoBoundingBox.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getGeoBoundingBox_Left(), ecorePackage.getEDouble(), "left", null, 0, 1, GeoBoundingBox.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getGeoBoundingBox_Right(), ecorePackage.getEDouble(), "right", null, 0, 1, GeoBoundingBox.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		// Initialize enums and add enum literals
		initEEnum(directionEEnum, Direction.class, "Direction"); //$NON-NLS-1$
		addEEnumLiteral(directionEEnum, Direction.UNDEFINED);
		addEEnumLiteral(directionEEnum, Direction.N);
		addEEnumLiteral(directionEEnum, Direction.NNE);
		addEEnumLiteral(directionEEnum, Direction.NE);
		addEEnumLiteral(directionEEnum, Direction.ENE);
		addEEnumLiteral(directionEEnum, Direction.E);
		addEEnumLiteral(directionEEnum, Direction.ESE);
		addEEnumLiteral(directionEEnum, Direction.SE);
		addEEnumLiteral(directionEEnum, Direction.SSE);
		addEEnumLiteral(directionEEnum, Direction.S);
		addEEnumLiteral(directionEEnum, Direction.SSW);
		addEEnumLiteral(directionEEnum, Direction.SW);
		addEEnumLiteral(directionEEnum, Direction.WSW);
		addEEnumLiteral(directionEEnum, Direction.W);
		addEEnumLiteral(directionEEnum, Direction.WNW);
		addEEnumLiteral(directionEEnum, Direction.NW);
		addEEnumLiteral(directionEEnum, Direction.NNW);

		initEEnum(latitudeHemisphereEEnum, LatitudeHemisphere.class, "LatitudeHemisphere"); //$NON-NLS-1$
		addEEnumLiteral(latitudeHemisphereEEnum, LatitudeHemisphere.N);
		addEEnumLiteral(latitudeHemisphereEEnum, LatitudeHemisphere.S);

		initEEnum(longitudeHemisphereEEnum, LongitudeHemisphere.class, "LongitudeHemisphere"); //$NON-NLS-1$
		addEEnumLiteral(longitudeHemisphereEEnum, LongitudeHemisphere.W);
		addEEnumLiteral(longitudeHemisphereEEnum, LongitudeHemisphere.E);

		initEEnum(relativeDepthMeasurementPositionEEnum, RelativeDepthMeasurementPosition.class, "RelativeDepthMeasurementPosition"); //$NON-NLS-1$
		addEEnumLiteral(relativeDepthMeasurementPositionEEnum, RelativeDepthMeasurementPosition.UNKNOWN);
		addEEnumLiteral(relativeDepthMeasurementPositionEEnum, RelativeDepthMeasurementPosition.TRANSDUCER);
		addEEnumLiteral(relativeDepthMeasurementPositionEEnum, RelativeDepthMeasurementPosition.SURFACE);
		addEEnumLiteral(relativeDepthMeasurementPositionEEnum, RelativeDepthMeasurementPosition.KEEL);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public interface Literals {
		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.GeoPositionImpl <em>Position</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.GeoPositionImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getGeoPosition()
		 * @generated
		 */
		public static final EClass GEO_POSITION = eINSTANCE.getGeoPosition();

		/**
		 * The meta object literal for the '<em><b>Longitude</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference GEO_POSITION__LONGITUDE = eINSTANCE.getGeoPosition_Longitude();

		/**
		 * The meta object literal for the '<em><b>Latitude</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference GEO_POSITION__LATITUDE = eINSTANCE.getGeoPosition_Latitude();

		/**
		 * The meta object literal for the '<em><b>Precision</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute GEO_POSITION__PRECISION = eINSTANCE.getGeoPosition_Precision();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.CoordinateImpl <em>Coordinate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.CoordinateImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getCoordinate()
		 * @generated
		 */
		public static final EClass COORDINATE = eINSTANCE.getCoordinate();

		/**
		 * The meta object literal for the '<em><b>Decimal Degree</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute COORDINATE__DECIMAL_DEGREE = eINSTANCE.getCoordinate_DecimalDegree();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.LatitudeImpl <em>Latitude</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.LatitudeImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getLatitude()
		 * @generated
		 */
		public static final EClass LATITUDE = eINSTANCE.getLatitude();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.LongitudeImpl <em>Longitude</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.LongitudeImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getLongitude()
		 * @generated
		 */
		public static final EClass LONGITUDE = eINSTANCE.getLongitude();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.RouteImpl <em>Route</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.RouteImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getRoute()
		 * @generated
		 */
		public static final EClass ROUTE = eINSTANCE.getRoute();

		/**
		 * The meta object literal for the '<em><b>Waypoints</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference ROUTE__WAYPOINTS = eINSTANCE.getRoute_Waypoints();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.NamedArtifactImpl <em>Named Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.NamedArtifactImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getNamedArtifact()
		 * @generated
		 */
		public static final EClass NAMED_ARTIFACT = eINSTANCE.getNamedArtifact();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute NAMED_ARTIFACT__NAME = eINSTANCE.getNamedArtifact_Name();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.NamedPositionImpl <em>Named Position</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.NamedPositionImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getNamedPosition()
		 * @generated
		 */
		public static final EClass NAMED_POSITION = eINSTANCE.getNamedPosition();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.RoutingContainerImpl <em>Routing Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.RoutingContainerImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getRoutingContainer()
		 * @generated
		 */
		public static final EClass ROUTING_CONTAINER = eINSTANCE.getRoutingContainer();

		/**
		 * The meta object literal for the '<em><b>Routes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference ROUTING_CONTAINER__ROUTES = eINSTANCE.getRoutingContainer_Routes();

		/**
		 * The meta object literal for the '<em><b>Area</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference ROUTING_CONTAINER__AREA = eINSTANCE.getRoutingContainer_Area();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.POIContainerImpl <em>POI Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.POIContainerImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getPOIContainer()
		 * @generated
		 */
		public static final EClass POI_CONTAINER = eINSTANCE.getPOIContainer();

		/**
		 * The meta object literal for the '<em><b>Pois</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference POI_CONTAINER__POIS = eINSTANCE.getPOIContainer_Pois();

		/**
		 * The meta object literal for the '<em><b>Area</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference POI_CONTAINER__AREA = eINSTANCE.getPOIContainer_Area();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.ChartContainerImpl <em>Chart Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.ChartContainerImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getChartContainer()
		 * @generated
		 */
		public static final EClass CHART_CONTAINER = eINSTANCE.getChartContainer();

		/**
		 * The meta object literal for the '<em><b>Charts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference CHART_CONTAINER__CHARTS = eINSTANCE.getChartContainer_Charts();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.NavigationCompoundImpl <em>Navigation Compound</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.NavigationCompoundImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getNavigationCompound()
		 * @generated
		 */
		public static final EClass NAVIGATION_COMPOUND = eINSTANCE.getNavigationCompound();

		/**
		 * The meta object literal for the '<em><b>Poi Container</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference NAVIGATION_COMPOUND__POI_CONTAINER = eINSTANCE.getNavigationCompound_PoiContainer();

		/**
		 * The meta object literal for the '<em><b>Routing Container</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference NAVIGATION_COMPOUND__ROUTING_CONTAINER = eINSTANCE.getNavigationCompound_RoutingContainer();

		/**
		 * The meta object literal for the '<em><b>Tracks Container</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference NAVIGATION_COMPOUND__TRACKS_CONTAINER = eINSTANCE.getNavigationCompound_TracksContainer();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.ChartImpl <em>Chart</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.ChartImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getChart()
		 * @generated
		 */
		public static final EClass CHART = eINSTANCE.getChart();

		/**
		 * The meta object literal for the '<em><b>Chart Configuration</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference CHART__CHART_CONFIGURATION = eINSTANCE.getChart_ChartConfiguration();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.GeoPosition3DImpl <em>Position3 D</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.GeoPosition3DImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getGeoPosition3D()
		 * @generated
		 */
		public static final EClass GEO_POSITION3_D = eINSTANCE.getGeoPosition3D();

		/**
		 * The meta object literal for the '<em><b>Altitude</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute GEO_POSITION3_D__ALTITUDE = eINSTANCE.getGeoPosition3D_Altitude();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.MeasuredPosition3DImpl <em>Measured Position3 D</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.MeasuredPosition3DImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getMeasuredPosition3D()
		 * @generated
		 */
		public static final EClass MEASURED_POSITION3_D = eINSTANCE.getMeasuredPosition3D();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.TracksContainerImpl <em>Tracks Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.TracksContainerImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getTracksContainer()
		 * @generated
		 */
		public static final EClass TRACKS_CONTAINER = eINSTANCE.getTracksContainer();

		/**
		 * The meta object literal for the '<em><b>Tracks</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference TRACKS_CONTAINER__TRACKS = eINSTANCE.getTracksContainer_Tracks();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.TrackImpl <em>Track</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.TrackImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getTrack()
		 * @generated
		 */
		public static final EClass TRACK = eINSTANCE.getTrack();

		/**
		 * The meta object literal for the '<em><b>Measured Position</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference TRACK__MEASURED_POSITION = eINSTANCE.getTrack_MeasuredPosition();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.ChartSymbolImpl <em>Chart Symbol</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.ChartSymbolImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getChartSymbol()
		 * @generated
		 */
		public static final EClass CHART_SYMBOL = eINSTANCE.getChartSymbol();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.ChartAreaImpl <em>Chart Area</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.ChartAreaImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getChartArea()
		 * @generated
		 */
		public static final EClass CHART_AREA = eINSTANCE.getChartArea();

		/**
		 * The meta object literal for the '<em><b>Bounds</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference CHART_AREA__BOUNDS = eINSTANCE.getChartArea_Bounds();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.ChartWayImpl <em>Chart Way</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.ChartWayImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getChartWay()
		 * @generated
		 */
		public static final EClass CHART_WAY = eINSTANCE.getChartWay();

		/**
		 * The meta object literal for the '<em><b>Waypoints</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference CHART_WAY__WAYPOINTS = eINSTANCE.getChartWay_Waypoints();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.NavareaImpl <em>Navarea</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.NavareaImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getNavarea()
		 * @generated
		 */
		public static final EClass NAVAREA = eINSTANCE.getNavarea();

		/**
		 * The meta object literal for the '<em><b>Areanumber</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute NAVAREA__AREANUMBER = eINSTANCE.getNavarea_Areanumber();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.DepthImpl <em>Depth</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.DepthImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getDepth()
		 * @generated
		 */
		public static final EClass DEPTH = eINSTANCE.getDepth();

		/**
		 * The meta object literal for the '<em><b>Measurement Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute DEPTH__MEASUREMENT_POSITION = eINSTANCE.getDepth_MeasurementPosition();

		/**
		 * The meta object literal for the '<em><b>Depth</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute DEPTH__DEPTH = eINSTANCE.getDepth_Depth();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.GNSSMeasuredPositionImpl <em>GNSS Measured Position</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.GNSSMeasuredPositionImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getGNSSMeasuredPosition()
		 * @generated
		 */
		public static final EClass GNSS_MEASURED_POSITION = eINSTANCE.getGNSSMeasuredPosition();

		/**
		 * The meta object literal for the '<em><b>Hdop</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute GNSS_MEASURED_POSITION__HDOP = eINSTANCE.getGNSSMeasuredPosition_Hdop();

		/**
		 * The meta object literal for the '<em><b>Vdop</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute GNSS_MEASURED_POSITION__VDOP = eINSTANCE.getGNSSMeasuredPosition_Vdop();

		/**
		 * The meta object literal for the '<em><b>Pdop</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute GNSS_MEASURED_POSITION__PDOP = eINSTANCE.getGNSSMeasuredPosition_Pdop();

		/**
		 * The meta object literal for the '<em><b>Augmentation</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute GNSS_MEASURED_POSITION__AUGMENTATION = eINSTANCE.getGNSSMeasuredPosition_Augmentation();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.AnchorPositionImpl <em>Anchor Position</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.AnchorPositionImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getAnchorPosition()
		 * @generated
		 */
		public static final EClass ANCHOR_POSITION = eINSTANCE.getAnchorPosition();

		/**
		 * The meta object literal for the '<em><b>XExtent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute ANCHOR_POSITION__XEXTENT = eINSTANCE.getAnchorPosition_XExtent();

		/**
		 * The meta object literal for the '<em><b>YExtent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute ANCHOR_POSITION__YEXTENT = eINSTANCE.getAnchorPosition_YExtent();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.impl.GeoBoundingBoxImpl <em>Bounding Box</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.impl.GeoBoundingBoxImpl
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getGeoBoundingBox()
		 * @generated
		 */
		public static final EClass GEO_BOUNDING_BOX = eINSTANCE.getGeoBoundingBox();

		/**
		 * The meta object literal for the '<em><b>Top</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute GEO_BOUNDING_BOX__TOP = eINSTANCE.getGeoBoundingBox_Top();

		/**
		 * The meta object literal for the '<em><b>Bottom</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute GEO_BOUNDING_BOX__BOTTOM = eINSTANCE.getGeoBoundingBox_Bottom();

		/**
		 * The meta object literal for the '<em><b>Left</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute GEO_BOUNDING_BOX__LEFT = eINSTANCE.getGeoBoundingBox_Left();

		/**
		 * The meta object literal for the '<em><b>Right</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute GEO_BOUNDING_BOX__RIGHT = eINSTANCE.getGeoBoundingBox_Right();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.Direction <em>Direction</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.Direction
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getDirection()
		 * @generated
		 */
		public static final EEnum DIRECTION = eINSTANCE.getDirection();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.LatitudeHemisphere <em>Latitude Hemisphere</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.LatitudeHemisphere
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getLatitudeHemisphere()
		 * @generated
		 */
		public static final EEnum LATITUDE_HEMISPHERE = eINSTANCE.getLatitudeHemisphere();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.LongitudeHemisphere <em>Longitude Hemisphere</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.LongitudeHemisphere
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getLongitudeHemisphere()
		 * @generated
		 */
		public static final EEnum LONGITUDE_HEMISPHERE = eINSTANCE.getLongitudeHemisphere();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.RelativeDepthMeasurementPosition <em>Relative Depth Measurement Position</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.RelativeDepthMeasurementPosition
		 * @see net.sf.seesea.model.core.geo.impl.GeoPackageImpl#getRelativeDepthMeasurementPosition()
		 * @generated
		 */
		public static final EEnum RELATIVE_DEPTH_MEASUREMENT_POSITION = eINSTANCE.getRelativeDepthMeasurementPosition();

	}

} //GeoPackageImpl
