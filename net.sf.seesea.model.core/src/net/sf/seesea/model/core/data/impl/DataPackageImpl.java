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
package net.sf.seesea.model.core.data.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import net.sf.seesea.model.core.data.DataFactory;
import net.sf.seesea.model.core.data.Instruments;
import net.sf.seesea.model.core.data.Series;
import net.sf.seesea.model.core.diagramInterchange.impl.DiagramInterchangePackageImpl;
import net.sf.seesea.model.core.geo.impl.GeoPackageImpl;
import net.sf.seesea.model.core.geo.osm.impl.OsmPackageImpl;
import net.sf.seesea.model.core.impl.CorePackageImpl;
import net.sf.seesea.model.core.physx.impl.PhysxPackageImpl;
import net.sf.seesea.model.core.weather.impl.WeatherPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @see net.sf.seesea.model.core.data.DataFactory
 * @model kind="package"
 * @generated
 */
public class DataPackageImpl extends EPackageImpl {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNAME = "data"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_URI = "data"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_PREFIX = "data"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final DataPackageImpl eINSTANCE = net.sf.seesea.model.core.data.impl.DataPackageImpl.init();

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.data.impl.SeriesImpl <em>Series</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.data.impl.SeriesImpl
	 * @see net.sf.seesea.model.core.data.impl.DataPackageImpl#getSeries()
	 * @generated
	 */
	public static final int SERIES = 0;

	/**
	 * The feature id for the '<em><b>Measurement</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SERIES__MEASUREMENT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Series</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SERIES_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.data.impl.InstrumentsImpl <em>Instruments</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.data.impl.InstrumentsImpl
	 * @see net.sf.seesea.model.core.data.impl.DataPackageImpl#getInstruments()
	 * @generated
	 */
	public static final int INSTRUMENTS = 1;

	/**
	 * The feature id for the '<em><b>Position</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int INSTRUMENTS__POSITION = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Water Temperature</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int INSTRUMENTS__WATER_TEMPERATURE = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Satellites Visible</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int INSTRUMENTS__SATELLITES_VISIBLE = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Wind Vector</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int INSTRUMENTS__WIND_VECTOR = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Instruments</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int INSTRUMENTS_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 4;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass seriesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass instrumentsEClass = null;

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
	 * @see net.sf.seesea.model.core.data.impl.DataPackageImpl#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DataPackageImpl() {
		super(eNS_URI, ((EFactory)DataFactory.INSTANCE));
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
	 * <p>This method is used to initialize {@link DataPackageImpl#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DataPackageImpl init() {
		if (isInited) return (DataPackageImpl)EPackage.Registry.INSTANCE.getEPackage(DataPackageImpl.eNS_URI);

		// Obtain or create and register package
		DataPackageImpl theDataPackage = (DataPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DataPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		CorePackageImpl theCorePackage = (CorePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI) instanceof CorePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI) : CorePackageImpl.eINSTANCE);
		GeoPackageImpl theGeoPackage = (GeoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(GeoPackageImpl.eNS_URI) instanceof GeoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(GeoPackageImpl.eNS_URI) : GeoPackageImpl.eINSTANCE);
		OsmPackageImpl theOsmPackage = (OsmPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(OsmPackageImpl.eNS_URI) instanceof OsmPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OsmPackageImpl.eNS_URI) : OsmPackageImpl.eINSTANCE);
		PhysxPackageImpl thePhysxPackage = (PhysxPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PhysxPackageImpl.eNS_URI) instanceof PhysxPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PhysxPackageImpl.eNS_URI) : PhysxPackageImpl.eINSTANCE);
		WeatherPackageImpl theWeatherPackage = (WeatherPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(WeatherPackageImpl.eNS_URI) instanceof WeatherPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(WeatherPackageImpl.eNS_URI) : WeatherPackageImpl.eINSTANCE);
		DiagramInterchangePackageImpl theDiagramInterchangePackage = (DiagramInterchangePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackageImpl.eNS_URI) instanceof DiagramInterchangePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackageImpl.eNS_URI) : DiagramInterchangePackageImpl.eINSTANCE);

		// Create package meta-data objects
		theDataPackage.createPackageContents();
		theCorePackage.createPackageContents();
		theGeoPackage.createPackageContents();
		theOsmPackage.createPackageContents();
		thePhysxPackage.createPackageContents();
		theWeatherPackage.createPackageContents();
		theDiagramInterchangePackage.createPackageContents();

		// Initialize created meta-data
		theDataPackage.initializePackageContents();
		theCorePackage.initializePackageContents();
		theGeoPackage.initializePackageContents();
		theOsmPackage.initializePackageContents();
		thePhysxPackage.initializePackageContents();
		theWeatherPackage.initializePackageContents();
		theDiagramInterchangePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDataPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DataPackageImpl.eNS_URI, theDataPackage);
		return theDataPackage;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.data.Series <em>Series</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Series</em>'.
	 * @see net.sf.seesea.model.core.data.Series
	 * @generated
	 */
	public EClass getSeries() {
		return seriesEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link net.sf.seesea.model.core.data.Series#getMeasurement <em>Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Measurement</em>'.
	 * @see net.sf.seesea.model.core.data.Series#getMeasurement()
	 * @see #getSeries()
	 * @generated
	 */
	public EReference getSeries_Measurement() {
		return (EReference)seriesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.data.Instruments <em>Instruments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Instruments</em>'.
	 * @see net.sf.seesea.model.core.data.Instruments
	 * @generated
	 */
	public EClass getInstruments() {
		return instrumentsEClass;
	}

	/**
	 * Returns the meta object for the reference '{@link net.sf.seesea.model.core.data.Instruments#getPosition <em>Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Position</em>'.
	 * @see net.sf.seesea.model.core.data.Instruments#getPosition()
	 * @see #getInstruments()
	 * @generated
	 */
	public EReference getInstruments_Position() {
		return (EReference)instrumentsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.data.Instruments#getWaterTemperature <em>Water Temperature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Water Temperature</em>'.
	 * @see net.sf.seesea.model.core.data.Instruments#getWaterTemperature()
	 * @see #getInstruments()
	 * @generated
	 */
	public EReference getInstruments_WaterTemperature() {
		return (EReference)instrumentsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.data.Instruments#getSatellitesVisible <em>Satellites Visible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Satellites Visible</em>'.
	 * @see net.sf.seesea.model.core.data.Instruments#getSatellitesVisible()
	 * @see #getInstruments()
	 * @generated
	 */
	public EReference getInstruments_SatellitesVisible() {
		return (EReference)instrumentsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for the containment reference '{@link net.sf.seesea.model.core.data.Instruments#getWindVector <em>Wind Vector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Wind Vector</em>'.
	 * @see net.sf.seesea.model.core.data.Instruments#getWindVector()
	 * @see #getInstruments()
	 * @generated
	 */
	public EReference getInstruments_WindVector() {
		return (EReference)instrumentsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	public DataFactory getDataFactory() {
		return (DataFactory)getEFactoryInstance();
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
		seriesEClass = createEClass(SERIES);
		createEReference(seriesEClass, SERIES__MEASUREMENT);

		instrumentsEClass = createEClass(INSTRUMENTS);
		createEReference(instrumentsEClass, INSTRUMENTS__POSITION);
		createEReference(instrumentsEClass, INSTRUMENTS__WATER_TEMPERATURE);
		createEReference(instrumentsEClass, INSTRUMENTS__SATELLITES_VISIBLE);
		createEReference(instrumentsEClass, INSTRUMENTS__WIND_VECTOR);
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
		CorePackageImpl theCorePackage = (CorePackageImpl)EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI);
		PhysxPackageImpl thePhysxPackage = (PhysxPackageImpl)EPackage.Registry.INSTANCE.getEPackage(PhysxPackageImpl.eNS_URI);
		GeoPackageImpl theGeoPackage = (GeoPackageImpl)EPackage.Registry.INSTANCE.getEPackage(GeoPackageImpl.eNS_URI);
		WeatherPackageImpl theWeatherPackage = (WeatherPackageImpl)EPackage.Registry.INSTANCE.getEPackage(WeatherPackageImpl.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		seriesEClass.getESuperTypes().add(theCorePackage.getModelObject());
		instrumentsEClass.getESuperTypes().add(theCorePackage.getModelObject());

		// Initialize classes and features; add operations and parameters
		initEClass(seriesEClass, Series.class, "Series", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getSeries_Measurement(), thePhysxPackage.getMeasurement(), null, "measurement", null, 0, -1, Series.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(instrumentsEClass, Instruments.class, "Instruments", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getInstruments_Position(), theGeoPackage.getMeasuredPosition3D(), null, "position", null, 0, 1, Instruments.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getInstruments_WaterTemperature(), thePhysxPackage.getTemperature(), null, "waterTemperature", null, 0, 1, Instruments.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getInstruments_SatellitesVisible(), thePhysxPackage.getSatellitesVisible(), null, "satellitesVisible", null, 0, 1, Instruments.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getInstruments_WindVector(), theWeatherPackage.getWindMeasurement(), null, "windVector", null, 0, 1, Instruments.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
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
		 * The meta object literal for the '{@link net.sf.seesea.model.core.data.impl.SeriesImpl <em>Series</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.data.impl.SeriesImpl
		 * @see net.sf.seesea.model.core.data.impl.DataPackageImpl#getSeries()
		 * @generated
		 */
		public static final EClass SERIES = eINSTANCE.getSeries();

		/**
		 * The meta object literal for the '<em><b>Measurement</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference SERIES__MEASUREMENT = eINSTANCE.getSeries_Measurement();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.data.impl.InstrumentsImpl <em>Instruments</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.data.impl.InstrumentsImpl
		 * @see net.sf.seesea.model.core.data.impl.DataPackageImpl#getInstruments()
		 * @generated
		 */
		public static final EClass INSTRUMENTS = eINSTANCE.getInstruments();

		/**
		 * The meta object literal for the '<em><b>Position</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference INSTRUMENTS__POSITION = eINSTANCE.getInstruments_Position();

		/**
		 * The meta object literal for the '<em><b>Water Temperature</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference INSTRUMENTS__WATER_TEMPERATURE = eINSTANCE.getInstruments_WaterTemperature();

		/**
		 * The meta object literal for the '<em><b>Satellites Visible</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference INSTRUMENTS__SATELLITES_VISIBLE = eINSTANCE.getInstruments_SatellitesVisible();

		/**
		 * The meta object literal for the '<em><b>Wind Vector</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference INSTRUMENTS__WIND_VECTOR = eINSTANCE.getInstruments_WindVector();

	}

} //DataPackageImpl
