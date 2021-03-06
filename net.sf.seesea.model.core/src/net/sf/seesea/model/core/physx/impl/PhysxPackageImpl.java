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
package net.sf.seesea.model.core.physx.impl;

import net.sf.seesea.model.core.CorePackage;
import net.sf.seesea.model.core.data.DataPackage;
import net.sf.seesea.model.core.data.impl.DataPackageImpl;
import net.sf.seesea.model.core.diagramInterchange.DiagramInterchangePackage;
import net.sf.seesea.model.core.diagramInterchange.impl.DiagramInterchangePackageImpl;
import net.sf.seesea.model.core.geo.GeoPackage;
import net.sf.seesea.model.core.geo.impl.GeoPackageImpl;
import net.sf.seesea.model.core.geo.osm.OsmPackage;
import net.sf.seesea.model.core.geo.osm.impl.OsmPackageImpl;
import net.sf.seesea.model.core.impl.CorePackageImpl;
import net.sf.seesea.model.core.physx.Acceleration;
import net.sf.seesea.model.core.physx.CompositeMeasurement;
import net.sf.seesea.model.core.physx.Distance;
import net.sf.seesea.model.core.physx.DistanceType;
import net.sf.seesea.model.core.physx.HandOrientation;
import net.sf.seesea.model.core.physx.Heading;
import net.sf.seesea.model.core.physx.HeadingType;
import net.sf.seesea.model.core.physx.LengthUnit;
import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.model.core.physx.PhysxFactory;
import net.sf.seesea.model.core.physx.PhysxPackage;
import net.sf.seesea.model.core.physx.RelativeSpeed;
import net.sf.seesea.model.core.physx.RelativeWind;
import net.sf.seesea.model.core.physx.SatelliteInfo;
import net.sf.seesea.model.core.physx.SatellitesVisible;
import net.sf.seesea.model.core.physx.Speed;
import net.sf.seesea.model.core.physx.SpeedType;
import net.sf.seesea.model.core.physx.SpeedUnit;
import net.sf.seesea.model.core.physx.Temperature;
import net.sf.seesea.model.core.physx.TemperatureUnit;
import net.sf.seesea.model.core.physx.Time;
import net.sf.seesea.model.core.weather.WeatherPackage;
import net.sf.seesea.model.core.weather.impl.WeatherPackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PhysxPackageImpl extends EPackageImpl implements PhysxPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass temperatureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass speedEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass headingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass measurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass relativeWindEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass satelliteInfoEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass satellitesVisibleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass timeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass distanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass compositeMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass relativeSpeedEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass accelerationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum temperatureUnitEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum headingTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum speedUnitEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum handOrientationEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum lengthUnitEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum speedTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum distanceTypeEEnum = null;

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
	 * @see net.sf.seesea.model.core.physx.PhysxPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PhysxPackageImpl() {
		super(eNS_URI, PhysxFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link PhysxPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static PhysxPackage init() {
		if (isInited) return (PhysxPackage)EPackage.Registry.INSTANCE.getEPackage(PhysxPackage.eNS_URI);

		// Obtain or create and register package
		PhysxPackageImpl thePhysxPackage = (PhysxPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PhysxPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PhysxPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		CorePackageImpl theCorePackage = (CorePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CorePackage.eNS_URI) instanceof CorePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CorePackage.eNS_URI) : CorePackage.eINSTANCE);
		GeoPackageImpl theGeoPackage = (GeoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(GeoPackage.eNS_URI) instanceof GeoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(GeoPackage.eNS_URI) : GeoPackage.eINSTANCE);
		OsmPackageImpl theOsmPackage = (OsmPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(OsmPackage.eNS_URI) instanceof OsmPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OsmPackage.eNS_URI) : OsmPackage.eINSTANCE);
		WeatherPackageImpl theWeatherPackage = (WeatherPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(WeatherPackage.eNS_URI) instanceof WeatherPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(WeatherPackage.eNS_URI) : WeatherPackage.eINSTANCE);
		DataPackageImpl theDataPackage = (DataPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DataPackage.eNS_URI) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DataPackage.eNS_URI) : DataPackage.eINSTANCE);
		DiagramInterchangePackageImpl theDiagramInterchangePackage = (DiagramInterchangePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackage.eNS_URI) instanceof DiagramInterchangePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackage.eNS_URI) : DiagramInterchangePackage.eINSTANCE);

		// Create package meta-data objects
		thePhysxPackage.createPackageContents();
		theCorePackage.createPackageContents();
		theGeoPackage.createPackageContents();
		theOsmPackage.createPackageContents();
		theWeatherPackage.createPackageContents();
		theDataPackage.createPackageContents();
		theDiagramInterchangePackage.createPackageContents();

		// Initialize created meta-data
		thePhysxPackage.initializePackageContents();
		theCorePackage.initializePackageContents();
		theGeoPackage.initializePackageContents();
		theOsmPackage.initializePackageContents();
		theWeatherPackage.initializePackageContents();
		theDataPackage.initializePackageContents();
		theDiagramInterchangePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePhysxPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(PhysxPackage.eNS_URI, thePhysxPackage);
		return thePhysxPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTemperature() {
		return temperatureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTemperature_Value() {
		return (EAttribute)temperatureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTemperature_Unit() {
		return (EAttribute)temperatureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpeed() {
		return speedEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpeed_Speed() {
		return (EAttribute)speedEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSpeed_SpeedUnit() {
		return (EAttribute)speedEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHeading() {
		return headingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHeading_Degrees() {
		return (EAttribute)headingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHeading_HeadingType() {
		return (EAttribute)headingEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMeasurement() {
		return measurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMeasurement_SensorID() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMeasurement_Time() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMeasurement_Timezone() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMeasurement_Valid() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMeasurement_Relative() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRelativeWind() {
		return relativeWindEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRelativeWind_BowOrientation() {
		return (EAttribute)relativeWindEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSatelliteInfo() {
		return satelliteInfoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSatelliteInfo_Id() {
		return (EAttribute)satelliteInfoEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSatelliteInfo_Elevation() {
		return (EAttribute)satelliteInfoEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSatelliteInfo_Azimuth() {
		return (EAttribute)satelliteInfoEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSatelliteInfo_SignalStrength() {
		return (EAttribute)satelliteInfoEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSatellitesVisible() {
		return satellitesVisibleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSatellitesVisible_SatelliteInfo() {
		return (EReference)satellitesVisibleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTime() {
		return timeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDistance() {
		return distanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDistance_Value() {
		return (EAttribute)distanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDistance_Unit() {
		return (EAttribute)distanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDistance_DistanceType() {
		return (EAttribute)distanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCompositeMeasurement() {
		return compositeMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCompositeMeasurement_Measurements() {
		return (EReference)compositeMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRelativeSpeed() {
		return relativeSpeedEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRelativeSpeed_Key() {
		return (EAttribute)relativeSpeedEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRelativeSpeed_Value() {
		return (EReference)relativeSpeedEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAcceleration() {
		return accelerationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAcceleration_X() {
		return (EAttribute)accelerationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAcceleration_Y() {
		return (EAttribute)accelerationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAcceleration_Z() {
		return (EAttribute)accelerationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getTemperatureUnit() {
		return temperatureUnitEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getHeadingType() {
		return headingTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSpeedUnit() {
		return speedUnitEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getHandOrientation() {
		return handOrientationEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLengthUnit() {
		return lengthUnitEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSpeedType() {
		return speedTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDistanceType() {
		return distanceTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysxFactory getPhysxFactory() {
		return (PhysxFactory)getEFactoryInstance();
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
		temperatureEClass = createEClass(TEMPERATURE);
		createEAttribute(temperatureEClass, TEMPERATURE__VALUE);
		createEAttribute(temperatureEClass, TEMPERATURE__UNIT);

		speedEClass = createEClass(SPEED);
		createEAttribute(speedEClass, SPEED__SPEED);
		createEAttribute(speedEClass, SPEED__SPEED_UNIT);

		headingEClass = createEClass(HEADING);
		createEAttribute(headingEClass, HEADING__DEGREES);
		createEAttribute(headingEClass, HEADING__HEADING_TYPE);

		measurementEClass = createEClass(MEASUREMENT);
		createEAttribute(measurementEClass, MEASUREMENT__SENSOR_ID);
		createEAttribute(measurementEClass, MEASUREMENT__TIME);
		createEAttribute(measurementEClass, MEASUREMENT__TIMEZONE);
		createEAttribute(measurementEClass, MEASUREMENT__VALID);
		createEAttribute(measurementEClass, MEASUREMENT__RELATIVE);

		relativeWindEClass = createEClass(RELATIVE_WIND);
		createEAttribute(relativeWindEClass, RELATIVE_WIND__BOW_ORIENTATION);

		satelliteInfoEClass = createEClass(SATELLITE_INFO);
		createEAttribute(satelliteInfoEClass, SATELLITE_INFO__ID);
		createEAttribute(satelliteInfoEClass, SATELLITE_INFO__ELEVATION);
		createEAttribute(satelliteInfoEClass, SATELLITE_INFO__AZIMUTH);
		createEAttribute(satelliteInfoEClass, SATELLITE_INFO__SIGNAL_STRENGTH);

		satellitesVisibleEClass = createEClass(SATELLITES_VISIBLE);
		createEReference(satellitesVisibleEClass, SATELLITES_VISIBLE__SATELLITE_INFO);

		timeEClass = createEClass(TIME);

		distanceEClass = createEClass(DISTANCE);
		createEAttribute(distanceEClass, DISTANCE__VALUE);
		createEAttribute(distanceEClass, DISTANCE__UNIT);
		createEAttribute(distanceEClass, DISTANCE__DISTANCE_TYPE);

		compositeMeasurementEClass = createEClass(COMPOSITE_MEASUREMENT);
		createEReference(compositeMeasurementEClass, COMPOSITE_MEASUREMENT__MEASUREMENTS);

		relativeSpeedEClass = createEClass(RELATIVE_SPEED);
		createEAttribute(relativeSpeedEClass, RELATIVE_SPEED__KEY);
		createEReference(relativeSpeedEClass, RELATIVE_SPEED__VALUE);

		accelerationEClass = createEClass(ACCELERATION);
		createEAttribute(accelerationEClass, ACCELERATION__X);
		createEAttribute(accelerationEClass, ACCELERATION__Y);
		createEAttribute(accelerationEClass, ACCELERATION__Z);

		// Create enums
		temperatureUnitEEnum = createEEnum(TEMPERATURE_UNIT);
		headingTypeEEnum = createEEnum(HEADING_TYPE);
		speedUnitEEnum = createEEnum(SPEED_UNIT);
		handOrientationEEnum = createEEnum(HAND_ORIENTATION);
		lengthUnitEEnum = createEEnum(LENGTH_UNIT);
		speedTypeEEnum = createEEnum(SPEED_TYPE);
		distanceTypeEEnum = createEEnum(DISTANCE_TYPE);
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
		CorePackage theCorePackage = (CorePackage)EPackage.Registry.INSTANCE.getEPackage(CorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		temperatureEClass.getESuperTypes().add(this.getMeasurement());
		speedEClass.getESuperTypes().add(this.getMeasurement());
		headingEClass.getESuperTypes().add(this.getMeasurement());
		measurementEClass.getESuperTypes().add(theCorePackage.getModelObject());
		relativeWindEClass.getESuperTypes().add(this.getHeading());
		relativeWindEClass.getESuperTypes().add(this.getSpeed());
		satelliteInfoEClass.getESuperTypes().add(this.getMeasurement());
		satellitesVisibleEClass.getESuperTypes().add(this.getMeasurement());
		timeEClass.getESuperTypes().add(this.getMeasurement());
		distanceEClass.getESuperTypes().add(this.getMeasurement());
		compositeMeasurementEClass.getESuperTypes().add(this.getMeasurement());
		relativeSpeedEClass.getESuperTypes().add(this.getMeasurement());
		accelerationEClass.getESuperTypes().add(this.getMeasurement());

		// Initialize classes and features; add operations and parameters
		initEClass(temperatureEClass, Temperature.class, "Temperature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getTemperature_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, Temperature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getTemperature_Unit(), this.getTemperatureUnit(), "unit", null, 0, 1, Temperature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(speedEClass, Speed.class, "Speed", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getSpeed_Speed(), ecorePackage.getEDouble(), "speed", null, 0, 1, Speed.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getSpeed_SpeedUnit(), this.getSpeedUnit(), "speedUnit", null, 0, 1, Speed.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(headingEClass, Heading.class, "Heading", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getHeading_Degrees(), ecorePackage.getEDouble(), "degrees", null, 0, 1, Heading.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getHeading_HeadingType(), this.getHeadingType(), "headingType", null, 0, 1, Heading.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(measurementEClass, Measurement.class, "Measurement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getMeasurement_SensorID(), ecorePackage.getEString(), "sensorID", null, 0, 1, Measurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getMeasurement_Time(), ecorePackage.getEDate(), "time", null, 0, 1, Measurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getMeasurement_Timezone(), ecorePackage.getEString(), "timezone", null, 0, 1, Measurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getMeasurement_Valid(), ecorePackage.getEBoolean(), "valid", null, 0, 1, Measurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getMeasurement_Relative(), ecorePackage.getEBoolean(), "relative", "true", 0, 1, Measurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass(relativeWindEClass, RelativeWind.class, "RelativeWind", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getRelativeWind_BowOrientation(), this.getHandOrientation(), "bowOrientation", null, 0, 1, RelativeWind.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(satelliteInfoEClass, SatelliteInfo.class, "SatelliteInfo", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getSatelliteInfo_Id(), ecorePackage.getEInt(), "id", null, 0, 1, SatelliteInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getSatelliteInfo_Elevation(), ecorePackage.getEInt(), "elevation", null, 0, 1, SatelliteInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getSatelliteInfo_Azimuth(), ecorePackage.getEInt(), "azimuth", null, 0, 1, SatelliteInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getSatelliteInfo_SignalStrength(), ecorePackage.getEInt(), "signalStrength", null, 0, 1, SatelliteInfo.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(satellitesVisibleEClass, SatellitesVisible.class, "SatellitesVisible", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getSatellitesVisible_SatelliteInfo(), this.getSatelliteInfo(), null, "satelliteInfo", null, 0, -1, SatellitesVisible.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(timeEClass, Time.class, "Time", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(distanceEClass, Distance.class, "Distance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getDistance_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, Distance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getDistance_Unit(), this.getLengthUnit(), "unit", null, 0, 1, Distance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getDistance_DistanceType(), this.getDistanceType(), "distanceType", null, 0, 1, Distance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(compositeMeasurementEClass, CompositeMeasurement.class, "CompositeMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getCompositeMeasurement_Measurements(), this.getMeasurement(), null, "measurements", null, 0, -1, CompositeMeasurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(relativeSpeedEClass, RelativeSpeed.class, "RelativeSpeed", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getRelativeSpeed_Key(), this.getSpeedType(), "key", null, 0, 1, RelativeSpeed.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getRelativeSpeed_Value(), this.getSpeed(), null, "value", null, 0, 1, RelativeSpeed.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(accelerationEClass, Acceleration.class, "Acceleration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getAcceleration_X(), ecorePackage.getEDouble(), "x", null, 0, 1, Acceleration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getAcceleration_Y(), ecorePackage.getEDouble(), "y", null, 0, 1, Acceleration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getAcceleration_Z(), ecorePackage.getEDouble(), "z", null, 0, 1, Acceleration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		// Initialize enums and add enum literals
		initEEnum(temperatureUnitEEnum, TemperatureUnit.class, "TemperatureUnit"); //$NON-NLS-1$
		addEEnumLiteral(temperatureUnitEEnum, TemperatureUnit.CELSIUS);
		addEEnumLiteral(temperatureUnitEEnum, TemperatureUnit.FAHRENHEIT);
		addEEnumLiteral(temperatureUnitEEnum, TemperatureUnit.KELVIN);

		initEEnum(headingTypeEEnum, HeadingType.class, "HeadingType"); //$NON-NLS-1$
		addEEnumLiteral(headingTypeEEnum, HeadingType.UNKNOWN);
		addEEnumLiteral(headingTypeEEnum, HeadingType.COMPASS);
		addEEnumLiteral(headingTypeEEnum, HeadingType.MAGNETIC);
		addEEnumLiteral(headingTypeEEnum, HeadingType.TRUE);
		addEEnumLiteral(headingTypeEEnum, HeadingType.COURSE_THROUGH_WATER);
		addEEnumLiteral(headingTypeEEnum, HeadingType.COG);
		addEEnumLiteral(headingTypeEEnum, HeadingType.RELATIVE);

		initEEnum(speedUnitEEnum, SpeedUnit.class, "SpeedUnit"); //$NON-NLS-1$
		addEEnumLiteral(speedUnitEEnum, SpeedUnit.K);
		addEEnumLiteral(speedUnitEEnum, SpeedUnit.M);
		addEEnumLiteral(speedUnitEEnum, SpeedUnit.N);
		addEEnumLiteral(speedUnitEEnum, SpeedUnit.UNKNOWN);

		initEEnum(handOrientationEEnum, HandOrientation.class, "HandOrientation"); //$NON-NLS-1$
		addEEnumLiteral(handOrientationEEnum, HandOrientation.UNKNOWN);
		addEEnumLiteral(handOrientationEEnum, HandOrientation.LEFT);
		addEEnumLiteral(handOrientationEEnum, HandOrientation.RIGHT);

		initEEnum(lengthUnitEEnum, LengthUnit.class, "LengthUnit"); //$NON-NLS-1$
		addEEnumLiteral(lengthUnitEEnum, LengthUnit.UNDEFINED);
		addEEnumLiteral(lengthUnitEEnum, LengthUnit.METERS);
		addEEnumLiteral(lengthUnitEEnum, LengthUnit.FEET);
		addEEnumLiteral(lengthUnitEEnum, LengthUnit.NAUTICAL_MILE);

		initEEnum(speedTypeEEnum, SpeedType.class, "SpeedType"); //$NON-NLS-1$
		addEEnumLiteral(speedTypeEEnum, SpeedType.UNKNOWN);
		addEEnumLiteral(speedTypeEEnum, SpeedType.COG);
		addEEnumLiteral(speedTypeEEnum, SpeedType.SPEEDTHOUGHWATER);

		initEEnum(distanceTypeEEnum, DistanceType.class, "DistanceType"); //$NON-NLS-1$
		addEEnumLiteral(distanceTypeEEnum, DistanceType.UNKNOWN);
		addEEnumLiteral(distanceTypeEEnum, DistanceType.TRIP);
		addEEnumLiteral(distanceTypeEEnum, DistanceType.TOTAL);
	}

} //PhysxPackageImpl
