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
package net.sf.seesea.model.core.geo.osm.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import net.sf.seesea.model.core.data.impl.DataPackageImpl;
import net.sf.seesea.model.core.diagramInterchange.impl.DiagramInterchangePackageImpl;
import net.sf.seesea.model.core.geo.impl.GeoPackageImpl;
import net.sf.seesea.model.core.geo.osm.Area;
import net.sf.seesea.model.core.geo.osm.OsmFactory;
import net.sf.seesea.model.core.geo.osm.World;
import net.sf.seesea.model.core.impl.CorePackageImpl;
import net.sf.seesea.model.core.physx.impl.PhysxPackageImpl;
import net.sf.seesea.model.core.weather.impl.WeatherPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @see net.sf.seesea.model.core.geo.osm.OsmFactory
 * @model kind="package"
 * @generated
 */
public class OsmPackageImpl extends EPackageImpl {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNAME = "osm"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_URI = "osm"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_PREFIX = "osm"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final OsmPackageImpl eINSTANCE = net.sf.seesea.model.core.geo.osm.impl.OsmPackageImpl.init();

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.osm.impl.AreaImpl <em>Area</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.osm.impl.AreaImpl
	 * @see net.sf.seesea.model.core.geo.osm.impl.OsmPackageImpl#getArea()
	 * @generated
	 */
	public static final int AREA = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int AREA__NAME = GeoPackageImpl.CHART__NAME;

	/**
	 * The feature id for the '<em><b>Chart Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int AREA__CHART_CONFIGURATION = GeoPackageImpl.CHART__CHART_CONFIGURATION;

	/**
	 * The feature id for the '<em><b>Poi Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int AREA__POI_CONTAINER = GeoPackageImpl.CHART_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Routing Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int AREA__ROUTING_CONTAINER = GeoPackageImpl.CHART_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Tracks Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int AREA__TRACKS_CONTAINER = GeoPackageImpl.CHART_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Zoom Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int AREA__ZOOM_LEVEL = GeoPackageImpl.CHART_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Map Center Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int AREA__MAP_CENTER_POSITION = GeoPackageImpl.CHART_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Sub Area</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int AREA__SUB_AREA = GeoPackageImpl.CHART_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Area</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int AREA_FEATURE_COUNT = GeoPackageImpl.CHART_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.geo.osm.impl.WorldImpl <em>World</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.geo.osm.impl.WorldImpl
	 * @see net.sf.seesea.model.core.geo.osm.impl.OsmPackageImpl#getWorld()
	 * @generated
	 */
	public static final int WORLD = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__NAME = AREA__NAME;

	/**
	 * The feature id for the '<em><b>Chart Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__CHART_CONFIGURATION = AREA__CHART_CONFIGURATION;

	/**
	 * The feature id for the '<em><b>Poi Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__POI_CONTAINER = AREA__POI_CONTAINER;

	/**
	 * The feature id for the '<em><b>Routing Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__ROUTING_CONTAINER = AREA__ROUTING_CONTAINER;

	/**
	 * The feature id for the '<em><b>Tracks Container</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__TRACKS_CONTAINER = AREA__TRACKS_CONTAINER;

	/**
	 * The feature id for the '<em><b>Zoom Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__ZOOM_LEVEL = AREA__ZOOM_LEVEL;

	/**
	 * The feature id for the '<em><b>Map Center Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__MAP_CENTER_POSITION = AREA__MAP_CENTER_POSITION;

	/**
	 * The feature id for the '<em><b>Sub Area</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__SUB_AREA = AREA__SUB_AREA;

	/**
	 * The feature id for the '<em><b>Longitude Scale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__LONGITUDE_SCALE = AREA_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Latitude Scale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__LATITUDE_SCALE = AREA_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Anchor Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__ANCHOR_POSITION = AREA_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cursor Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__CURSOR_POSITION = AREA_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Trip</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__TRIP = AREA_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Total Trip</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD__TOTAL_TRIP = AREA_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>World</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int WORLD_FEATURE_COUNT = AREA_FEATURE_COUNT + 6;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass areaEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass worldEClass = null;

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
	 * @see net.sf.seesea.model.core.geo.osm.impl.OsmPackageImpl#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private OsmPackageImpl() {
		super(eNS_URI, ((EFactory)OsmFactory.INSTANCE));
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
	 * <p>This method is used to initialize {@link OsmPackageImpl#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static OsmPackageImpl init() {
		if (isInited) return (OsmPackageImpl)EPackage.Registry.INSTANCE.getEPackage(OsmPackageImpl.eNS_URI);

		// Obtain or create and register package
		OsmPackageImpl theOsmPackage = (OsmPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof OsmPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new OsmPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		CorePackageImpl theCorePackage = (CorePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI) instanceof CorePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI) : CorePackageImpl.eINSTANCE);
		GeoPackageImpl theGeoPackage = (GeoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(GeoPackageImpl.eNS_URI) instanceof GeoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(GeoPackageImpl.eNS_URI) : GeoPackageImpl.eINSTANCE);
		PhysxPackageImpl thePhysxPackage = (PhysxPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PhysxPackageImpl.eNS_URI) instanceof PhysxPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PhysxPackageImpl.eNS_URI) : PhysxPackageImpl.eINSTANCE);
		WeatherPackageImpl theWeatherPackage = (WeatherPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(WeatherPackageImpl.eNS_URI) instanceof WeatherPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(WeatherPackageImpl.eNS_URI) : WeatherPackageImpl.eINSTANCE);
		DataPackageImpl theDataPackage = (DataPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DataPackageImpl.eNS_URI) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DataPackageImpl.eNS_URI) : DataPackageImpl.eINSTANCE);
		DiagramInterchangePackageImpl theDiagramInterchangePackage = (DiagramInterchangePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackageImpl.eNS_URI) instanceof DiagramInterchangePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackageImpl.eNS_URI) : DiagramInterchangePackageImpl.eINSTANCE);

		// Create package meta-data objects
		theOsmPackage.createPackageContents();
		theCorePackage.createPackageContents();
		theGeoPackage.createPackageContents();
		thePhysxPackage.createPackageContents();
		theWeatherPackage.createPackageContents();
		theDataPackage.createPackageContents();
		theDiagramInterchangePackage.createPackageContents();

		// Initialize created meta-data
		theOsmPackage.initializePackageContents();
		theCorePackage.initializePackageContents();
		theGeoPackage.initializePackageContents();
		thePhysxPackage.initializePackageContents();
		theWeatherPackage.initializePackageContents();
		theDataPackage.initializePackageContents();
		theDiagramInterchangePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theOsmPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(OsmPackageImpl.eNS_URI, theOsmPackage);
		return theOsmPackage;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.osm.Area <em>Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Area</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.Area
	 * @generated
	 */
	public EClass getArea() {
		return areaEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.osm.Area#getZoomLevel <em>Zoom Level</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Zoom Level</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.Area#getZoomLevel()
	 * @see #getArea()
	 * @generated
	 */
	public EAttribute getArea_ZoomLevel() {
		return (EAttribute)areaEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.geo.osm.Area#getMapCenterPosition <em>Map Center Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Map Center Position</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.Area#getMapCenterPosition()
	 * @see #getArea()
	 * @generated
	 */
	public EReference getArea_MapCenterPosition() {
		return (EReference)areaEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the containment reference list '{@link net.sf.seesea.model.core.geo.osm.Area#getSubArea <em>Sub Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Area</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.Area#getSubArea()
	 * @see #getArea()
	 * @generated
	 */
	public EReference getArea_SubArea() {
		return (EReference)areaEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.geo.osm.World <em>World</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>World</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.World
	 * @generated
	 */
	public EClass getWorld() {
		return worldEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.osm.World#isLongitudeScale <em>Longitude Scale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Longitude Scale</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.World#isLongitudeScale()
	 * @see #getWorld()
	 * @generated
	 */
	public EAttribute getWorld_LongitudeScale() {
		return (EAttribute)worldEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.osm.World#isLatitudeScale <em>Latitude Scale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Latitude Scale</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.World#isLatitudeScale()
	 * @see #getWorld()
	 * @generated
	 */
	public EAttribute getWorld_LatitudeScale() {
		return (EAttribute)worldEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.geo.osm.World#getAnchorPosition <em>Anchor Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Anchor Position</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.World#getAnchorPosition()
	 * @see #getWorld()
	 * @generated
	 */
	public EReference getWorld_AnchorPosition() {
		return (EReference)worldEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.geo.osm.World#getCursorPosition <em>Cursor Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Cursor Position</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.World#getCursorPosition()
	 * @see #getWorld()
	 * @generated
	 */
	public EReference getWorld_CursorPosition() {
		return (EReference)worldEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.osm.World#getTrip <em>Trip</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Trip</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.World#getTrip()
	 * @see #getWorld()
	 * @generated
	 */
	public EAttribute getWorld_Trip() {
		return (EAttribute)worldEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.geo.osm.World#getTotalTrip <em>Total Trip</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Trip</em>'.
	 * @see net.sf.seesea.model.core.geo.osm.World#getTotalTrip()
	 * @see #getWorld()
	 * @generated
	 */
	public EAttribute getWorld_TotalTrip() {
		return (EAttribute)worldEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	public OsmFactory getOsmFactory() {
		return (OsmFactory)getEFactoryInstance();
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
		areaEClass = createEClass(AREA);
		createEAttribute(areaEClass, AREA__ZOOM_LEVEL);
		createEReference(areaEClass, AREA__MAP_CENTER_POSITION);
		createEReference(areaEClass, AREA__SUB_AREA);

		worldEClass = createEClass(WORLD);
		createEAttribute(worldEClass, WORLD__LONGITUDE_SCALE);
		createEAttribute(worldEClass, WORLD__LATITUDE_SCALE);
		createEReference(worldEClass, WORLD__ANCHOR_POSITION);
		createEReference(worldEClass, WORLD__CURSOR_POSITION);
		createEAttribute(worldEClass, WORLD__TRIP);
		createEAttribute(worldEClass, WORLD__TOTAL_TRIP);
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
		GeoPackageImpl theGeoPackage = (GeoPackageImpl)EPackage.Registry.INSTANCE.getEPackage(GeoPackageImpl.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		areaEClass.getESuperTypes().add(theGeoPackage.getChart());
		areaEClass.getESuperTypes().add(theGeoPackage.getNavigationCompound());
		worldEClass.getESuperTypes().add(this.getArea());

		// Initialize classes and features; add operations and parameters
		initEClass(areaEClass, Area.class, "Area", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getArea_ZoomLevel(), ecorePackage.getEInt(), "zoomLevel", "0", 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference(getArea_MapCenterPosition(), theGeoPackage.getGeoPosition(), null, "mapCenterPosition", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getArea_SubArea(), this.getArea(), null, "subArea", null, 0, -1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(worldEClass, World.class, "World", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getWorld_LongitudeScale(), ecorePackage.getEBoolean(), "longitudeScale", "true", 0, 1, World.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute(getWorld_LatitudeScale(), ecorePackage.getEBoolean(), "latitudeScale", "true", 0, 1, World.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEReference(getWorld_AnchorPosition(), theGeoPackage.getAnchorPosition(), null, "anchorPosition", null, 0, 1, World.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getWorld_CursorPosition(), theGeoPackage.getGeoPosition(), null, "cursorPosition", null, 0, 1, World.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getWorld_Trip(), ecorePackage.getEDouble(), "trip", null, 0, 1, World.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getWorld_TotalTrip(), ecorePackage.getEDouble(), "totalTrip", null, 0, 1, World.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
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
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.osm.impl.AreaImpl <em>Area</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.osm.impl.AreaImpl
		 * @see net.sf.seesea.model.core.geo.osm.impl.OsmPackageImpl#getArea()
		 * @generated
		 */
		public static final EClass AREA = eINSTANCE.getArea();

		/**
		 * The meta object literal for the '<em><b>Zoom Level</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute AREA__ZOOM_LEVEL = eINSTANCE.getArea_ZoomLevel();

		/**
		 * The meta object literal for the '<em><b>Map Center Position</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference AREA__MAP_CENTER_POSITION = eINSTANCE.getArea_MapCenterPosition();

		/**
		 * The meta object literal for the '<em><b>Sub Area</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference AREA__SUB_AREA = eINSTANCE.getArea_SubArea();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.geo.osm.impl.WorldImpl <em>World</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.geo.osm.impl.WorldImpl
		 * @see net.sf.seesea.model.core.geo.osm.impl.OsmPackageImpl#getWorld()
		 * @generated
		 */
		public static final EClass WORLD = eINSTANCE.getWorld();

		/**
		 * The meta object literal for the '<em><b>Longitude Scale</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute WORLD__LONGITUDE_SCALE = eINSTANCE.getWorld_LongitudeScale();

		/**
		 * The meta object literal for the '<em><b>Latitude Scale</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute WORLD__LATITUDE_SCALE = eINSTANCE.getWorld_LatitudeScale();

		/**
		 * The meta object literal for the '<em><b>Anchor Position</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference WORLD__ANCHOR_POSITION = eINSTANCE.getWorld_AnchorPosition();

		/**
		 * The meta object literal for the '<em><b>Cursor Position</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference WORLD__CURSOR_POSITION = eINSTANCE.getWorld_CursorPosition();

		/**
		 * The meta object literal for the '<em><b>Trip</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute WORLD__TRIP = eINSTANCE.getWorld_Trip();

		/**
		 * The meta object literal for the '<em><b>Total Trip</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute WORLD__TOTAL_TRIP = eINSTANCE.getWorld_TotalTrip();

	}

} //OsmPackageImpl
