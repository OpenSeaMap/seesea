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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import net.sf.seesea.model.core.data.impl.DataPackageImpl;
import net.sf.seesea.model.core.diagramInterchange.impl.DiagramInterchangePackageImpl;
import net.sf.seesea.model.core.geo.impl.GeoPackageImpl;
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
import net.sf.seesea.model.core.weather.impl.WeatherPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @see net.sf.seesea.model.core.physx.PhysxFactory
 * @model kind="package"
 * @generated
 */
public class PhysxPackageImpl extends EPackageImpl {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNAME = "physx"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_URI = "physx"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_PREFIX = "physx"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final PhysxPackageImpl eINSTANCE = net.sf.seesea.model.core.physx.impl.PhysxPackageImpl.init();

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.MeasurementImpl <em>Measurement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.MeasurementImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getMeasurement()
	 * @generated
	 */
	public static final int MEASUREMENT = 3;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASUREMENT__SENSOR_ID = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASUREMENT__TIME = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASUREMENT__TIMEZONE = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASUREMENT__VALID = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASUREMENT__RELATIVE = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int MEASUREMENT_FEATURE_COUNT = CorePackageImpl.MODEL_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.TemperatureImpl <em>Temperature</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.TemperatureImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getTemperature()
	 * @generated
	 */
	public static final int TEMPERATURE = 0;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TEMPERATURE__SENSOR_ID = MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TEMPERATURE__TIME = MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TEMPERATURE__TIMEZONE = MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TEMPERATURE__VALID = MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TEMPERATURE__RELATIVE = MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TEMPERATURE__VALUE = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TEMPERATURE__UNIT = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Temperature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TEMPERATURE_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.SpeedImpl <em>Speed</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.SpeedImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getSpeed()
	 * @generated
	 */
	public static final int SPEED = 1;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SPEED__SENSOR_ID = MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SPEED__TIME = MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SPEED__TIMEZONE = MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SPEED__VALID = MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SPEED__RELATIVE = MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SPEED__SPEED = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Speed Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SPEED__SPEED_UNIT = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Speed</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SPEED_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.HeadingImpl <em>Heading</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.HeadingImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getHeading()
	 * @generated
	 */
	public static final int HEADING = 2;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int HEADING__SENSOR_ID = MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int HEADING__TIME = MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int HEADING__TIMEZONE = MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int HEADING__VALID = MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int HEADING__RELATIVE = MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>Degrees</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int HEADING__DEGREES = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Heading Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int HEADING__HEADING_TYPE = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Heading</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int HEADING_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.RelativeWindImpl <em>Relative Wind</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.RelativeWindImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getRelativeWind()
	 * @generated
	 */
	public static final int RELATIVE_WIND = 4;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND__SENSOR_ID = HEADING__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND__TIME = HEADING__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND__TIMEZONE = HEADING__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND__VALID = HEADING__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND__RELATIVE = HEADING__RELATIVE;

	/**
	 * The feature id for the '<em><b>Degrees</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND__DEGREES = HEADING__DEGREES;

	/**
	 * The feature id for the '<em><b>Heading Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND__HEADING_TYPE = HEADING__HEADING_TYPE;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND__SPEED = HEADING_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Speed Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND__SPEED_UNIT = HEADING_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Bow Orientation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND__BOW_ORIENTATION = HEADING_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Relative Wind</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_WIND_FEATURE_COUNT = HEADING_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.SatelliteInfoImpl <em>Satellite Info</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.SatelliteInfoImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getSatelliteInfo()
	 * @generated
	 */
	public static final int SATELLITE_INFO = 5;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITE_INFO__SENSOR_ID = MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITE_INFO__TIME = MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITE_INFO__TIMEZONE = MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITE_INFO__VALID = MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITE_INFO__RELATIVE = MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITE_INFO__ID = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Elevation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITE_INFO__ELEVATION = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Azimuth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITE_INFO__AZIMUTH = MEASUREMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Signal Strength</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITE_INFO__SIGNAL_STRENGTH = MEASUREMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Satellite Info</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITE_INFO_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.SatellitesVisibleImpl <em>Satellites Visible</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.SatellitesVisibleImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getSatellitesVisible()
	 * @generated
	 */
	public static final int SATELLITES_VISIBLE = 6;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITES_VISIBLE__SENSOR_ID = MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITES_VISIBLE__TIME = MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITES_VISIBLE__TIMEZONE = MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITES_VISIBLE__VALID = MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITES_VISIBLE__RELATIVE = MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>Satellite Info</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITES_VISIBLE__SATELLITE_INFO = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Satellites Visible</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int SATELLITES_VISIBLE_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.TimeImpl <em>Time</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.TimeImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getTime()
	 * @generated
	 */
	public static final int TIME = 7;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TIME__SENSOR_ID = MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TIME__TIME = MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TIME__TIMEZONE = MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TIME__VALID = MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TIME__RELATIVE = MEASUREMENT__RELATIVE;

	/**
	 * The number of structural features of the '<em>Time</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TIME_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.DistanceImpl <em>Distance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.DistanceImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getDistance()
	 * @generated
	 */
	public static final int DISTANCE = 8;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DISTANCE__SENSOR_ID = MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DISTANCE__TIME = MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DISTANCE__TIMEZONE = MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DISTANCE__VALID = MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DISTANCE__RELATIVE = MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DISTANCE__VALUE = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DISTANCE__UNIT = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Distance Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DISTANCE__DISTANCE_TYPE = MEASUREMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Distance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DISTANCE_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.CompositeMeasurementImpl <em>Composite Measurement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.CompositeMeasurementImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getCompositeMeasurement()
	 * @generated
	 */
	public static final int COMPOSITE_MEASUREMENT = 9;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int COMPOSITE_MEASUREMENT__SENSOR_ID = MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int COMPOSITE_MEASUREMENT__TIME = MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int COMPOSITE_MEASUREMENT__TIMEZONE = MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int COMPOSITE_MEASUREMENT__VALID = MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int COMPOSITE_MEASUREMENT__RELATIVE = MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>Measurements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int COMPOSITE_MEASUREMENT__MEASUREMENTS = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Composite Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int COMPOSITE_MEASUREMENT_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.RelativeSpeedImpl <em>Relative Speed</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.RelativeSpeedImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getRelativeSpeed()
	 * @generated
	 */
	public static final int RELATIVE_SPEED = 10;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_SPEED__SENSOR_ID = MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_SPEED__TIME = MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_SPEED__TIMEZONE = MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_SPEED__VALID = MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_SPEED__RELATIVE = MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_SPEED__KEY = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_SPEED__VALUE = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Relative Speed</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int RELATIVE_SPEED_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.impl.AccelerationImpl <em>Acceleration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.impl.AccelerationImpl
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getAcceleration()
	 * @generated
	 */
	public static final int ACCELERATION = 11;

	/**
	 * The feature id for the '<em><b>Sensor ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ACCELERATION__SENSOR_ID = MEASUREMENT__SENSOR_ID;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ACCELERATION__TIME = MEASUREMENT__TIME;

	/**
	 * The feature id for the '<em><b>Timezone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ACCELERATION__TIMEZONE = MEASUREMENT__TIMEZONE;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ACCELERATION__VALID = MEASUREMENT__VALID;

	/**
	 * The feature id for the '<em><b>Relative</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ACCELERATION__RELATIVE = MEASUREMENT__RELATIVE;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ACCELERATION__X = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ACCELERATION__Y = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Z</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ACCELERATION__Z = MEASUREMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Acceleration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ACCELERATION_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.TemperatureUnit <em>Temperature Unit</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.TemperatureUnit
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getTemperatureUnit()
	 * @generated
	 */
	public static final int TEMPERATURE_UNIT = 12;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.HeadingType <em>Heading Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.HeadingType
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getHeadingType()
	 * @generated
	 */
	public static final int HEADING_TYPE = 13;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.SpeedUnit <em>Speed Unit</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.SpeedUnit
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getSpeedUnit()
	 * @generated
	 */
	public static final int SPEED_UNIT = 14;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.HandOrientation <em>Hand Orientation</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.HandOrientation
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getHandOrientation()
	 * @generated
	 */
	public static final int HAND_ORIENTATION = 15;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.LengthUnit <em>Length Unit</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.LengthUnit
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getLengthUnit()
	 * @generated
	 */
	public static final int LENGTH_UNIT = 16;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.SpeedType <em>Speed Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.SpeedType
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getSpeedType()
	 * @generated
	 */
	public static final int SPEED_TYPE = 17;

	/**
	 * The meta object id for the '{@link net.sf.seesea.model.core.physx.DistanceType <em>Distance Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.seesea.model.core.physx.DistanceType
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getDistanceType()
	 * @generated
	 */
	public static final int DISTANCE_TYPE = 18;

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
	 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PhysxPackageImpl() {
		super(eNS_URI, ((EFactory)PhysxFactory.INSTANCE));
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
	 * <p>This method is used to initialize {@link PhysxPackageImpl#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static PhysxPackageImpl init() {
		if (isInited) return (PhysxPackageImpl)EPackage.Registry.INSTANCE.getEPackage(PhysxPackageImpl.eNS_URI);

		// Obtain or create and register package
		PhysxPackageImpl thePhysxPackage = (PhysxPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PhysxPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PhysxPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		CorePackageImpl theCorePackage = (CorePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI) instanceof CorePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI) : CorePackageImpl.eINSTANCE);
		GeoPackageImpl theGeoPackage = (GeoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(GeoPackageImpl.eNS_URI) instanceof GeoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(GeoPackageImpl.eNS_URI) : GeoPackageImpl.eINSTANCE);
		OsmPackageImpl theOsmPackage = (OsmPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(OsmPackageImpl.eNS_URI) instanceof OsmPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OsmPackageImpl.eNS_URI) : OsmPackageImpl.eINSTANCE);
		WeatherPackageImpl theWeatherPackage = (WeatherPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(WeatherPackageImpl.eNS_URI) instanceof WeatherPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(WeatherPackageImpl.eNS_URI) : WeatherPackageImpl.eINSTANCE);
		DataPackageImpl theDataPackage = (DataPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DataPackageImpl.eNS_URI) instanceof DataPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DataPackageImpl.eNS_URI) : DataPackageImpl.eINSTANCE);
		DiagramInterchangePackageImpl theDiagramInterchangePackage = (DiagramInterchangePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackageImpl.eNS_URI) instanceof DiagramInterchangePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DiagramInterchangePackageImpl.eNS_URI) : DiagramInterchangePackageImpl.eINSTANCE);

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
		EPackage.Registry.INSTANCE.put(PhysxPackageImpl.eNS_URI, thePhysxPackage);
		return thePhysxPackage;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.Temperature <em>Temperature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Temperature</em>'.
	 * @see net.sf.seesea.model.core.physx.Temperature
	 * @generated
	 */
	public EClass getTemperature() {
		return temperatureEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Temperature#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.sf.seesea.model.core.physx.Temperature#getValue()
	 * @see #getTemperature()
	 * @generated
	 */
	public EAttribute getTemperature_Value() {
		return (EAttribute)temperatureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Temperature#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit</em>'.
	 * @see net.sf.seesea.model.core.physx.Temperature#getUnit()
	 * @see #getTemperature()
	 * @generated
	 */
	public EAttribute getTemperature_Unit() {
		return (EAttribute)temperatureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.Speed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Speed</em>'.
	 * @see net.sf.seesea.model.core.physx.Speed
	 * @generated
	 */
	public EClass getSpeed() {
		return speedEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Speed#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see net.sf.seesea.model.core.physx.Speed#getSpeed()
	 * @see #getSpeed()
	 * @generated
	 */
	public EAttribute getSpeed_Speed() {
		return (EAttribute)speedEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Speed#getSpeedUnit <em>Speed Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed Unit</em>'.
	 * @see net.sf.seesea.model.core.physx.Speed#getSpeedUnit()
	 * @see #getSpeed()
	 * @generated
	 */
	public EAttribute getSpeed_SpeedUnit() {
		return (EAttribute)speedEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.Heading <em>Heading</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Heading</em>'.
	 * @see net.sf.seesea.model.core.physx.Heading
	 * @generated
	 */
	public EClass getHeading() {
		return headingEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Heading#getDegrees <em>Degrees</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Degrees</em>'.
	 * @see net.sf.seesea.model.core.physx.Heading#getDegrees()
	 * @see #getHeading()
	 * @generated
	 */
	public EAttribute getHeading_Degrees() {
		return (EAttribute)headingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Heading#getHeadingType <em>Heading Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Heading Type</em>'.
	 * @see net.sf.seesea.model.core.physx.Heading#getHeadingType()
	 * @see #getHeading()
	 * @generated
	 */
	public EAttribute getHeading_HeadingType() {
		return (EAttribute)headingEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.Measurement <em>Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Measurement</em>'.
	 * @see net.sf.seesea.model.core.physx.Measurement
	 * @generated
	 */
	public EClass getMeasurement() {
		return measurementEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Measurement#getSensorID <em>Sensor ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sensor ID</em>'.
	 * @see net.sf.seesea.model.core.physx.Measurement#getSensorID()
	 * @see #getMeasurement()
	 * @generated
	 */
	public EAttribute getMeasurement_SensorID() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Measurement#getTime <em>Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time</em>'.
	 * @see net.sf.seesea.model.core.physx.Measurement#getTime()
	 * @see #getMeasurement()
	 * @generated
	 */
	public EAttribute getMeasurement_Time() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Measurement#getTimezone <em>Timezone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Timezone</em>'.
	 * @see net.sf.seesea.model.core.physx.Measurement#getTimezone()
	 * @see #getMeasurement()
	 * @generated
	 */
	public EAttribute getMeasurement_Timezone() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Measurement#isValid <em>Valid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Valid</em>'.
	 * @see net.sf.seesea.model.core.physx.Measurement#isValid()
	 * @see #getMeasurement()
	 * @generated
	 */
	public EAttribute getMeasurement_Valid() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Measurement#isRelative <em>Relative</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Relative</em>'.
	 * @see net.sf.seesea.model.core.physx.Measurement#isRelative()
	 * @see #getMeasurement()
	 * @generated
	 */
	public EAttribute getMeasurement_Relative() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.RelativeWind <em>Relative Wind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Relative Wind</em>'.
	 * @see net.sf.seesea.model.core.physx.RelativeWind
	 * @generated
	 */
	public EClass getRelativeWind() {
		return relativeWindEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.RelativeWind#getBowOrientation <em>Bow Orientation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bow Orientation</em>'.
	 * @see net.sf.seesea.model.core.physx.RelativeWind#getBowOrientation()
	 * @see #getRelativeWind()
	 * @generated
	 */
	public EAttribute getRelativeWind_BowOrientation() {
		return (EAttribute)relativeWindEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.SatelliteInfo <em>Satellite Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Satellite Info</em>'.
	 * @see net.sf.seesea.model.core.physx.SatelliteInfo
	 * @generated
	 */
	public EClass getSatelliteInfo() {
		return satelliteInfoEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.SatelliteInfo#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see net.sf.seesea.model.core.physx.SatelliteInfo#getId()
	 * @see #getSatelliteInfo()
	 * @generated
	 */
	public EAttribute getSatelliteInfo_Id() {
		return (EAttribute)satelliteInfoEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.SatelliteInfo#getElevation <em>Elevation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Elevation</em>'.
	 * @see net.sf.seesea.model.core.physx.SatelliteInfo#getElevation()
	 * @see #getSatelliteInfo()
	 * @generated
	 */
	public EAttribute getSatelliteInfo_Elevation() {
		return (EAttribute)satelliteInfoEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.SatelliteInfo#getAzimuth <em>Azimuth</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Azimuth</em>'.
	 * @see net.sf.seesea.model.core.physx.SatelliteInfo#getAzimuth()
	 * @see #getSatelliteInfo()
	 * @generated
	 */
	public EAttribute getSatelliteInfo_Azimuth() {
		return (EAttribute)satelliteInfoEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.SatelliteInfo#getSignalStrength <em>Signal Strength</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Signal Strength</em>'.
	 * @see net.sf.seesea.model.core.physx.SatelliteInfo#getSignalStrength()
	 * @see #getSatelliteInfo()
	 * @generated
	 */
	public EAttribute getSatelliteInfo_SignalStrength() {
		return (EAttribute)satelliteInfoEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.SatellitesVisible <em>Satellites Visible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Satellites Visible</em>'.
	 * @see net.sf.seesea.model.core.physx.SatellitesVisible
	 * @generated
	 */
	public EClass getSatellitesVisible() {
		return satellitesVisibleEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link net.sf.seesea.model.core.physx.SatellitesVisible#getSatelliteInfo <em>Satellite Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Satellite Info</em>'.
	 * @see net.sf.seesea.model.core.physx.SatellitesVisible#getSatelliteInfo()
	 * @see #getSatellitesVisible()
	 * @generated
	 */
	public EReference getSatellitesVisible_SatelliteInfo() {
		return (EReference)satellitesVisibleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.Time <em>Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time</em>'.
	 * @see net.sf.seesea.model.core.physx.Time
	 * @generated
	 */
	public EClass getTime() {
		return timeEClass;
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.Distance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Distance</em>'.
	 * @see net.sf.seesea.model.core.physx.Distance
	 * @generated
	 */
	public EClass getDistance() {
		return distanceEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Distance#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.sf.seesea.model.core.physx.Distance#getValue()
	 * @see #getDistance()
	 * @generated
	 */
	public EAttribute getDistance_Value() {
		return (EAttribute)distanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Distance#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit</em>'.
	 * @see net.sf.seesea.model.core.physx.Distance#getUnit()
	 * @see #getDistance()
	 * @generated
	 */
	public EAttribute getDistance_Unit() {
		return (EAttribute)distanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Distance#getDistanceType <em>Distance Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance Type</em>'.
	 * @see net.sf.seesea.model.core.physx.Distance#getDistanceType()
	 * @see #getDistance()
	 * @generated
	 */
	public EAttribute getDistance_DistanceType() {
		return (EAttribute)distanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.CompositeMeasurement <em>Composite Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Composite Measurement</em>'.
	 * @see net.sf.seesea.model.core.physx.CompositeMeasurement
	 * @generated
	 */
	public EClass getCompositeMeasurement() {
		return compositeMeasurementEClass;
	}

	/**
	 * Returns the meta object for the reference list '{@link net.sf.seesea.model.core.physx.CompositeMeasurement#getMeasurements <em>Measurements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Measurements</em>'.
	 * @see net.sf.seesea.model.core.physx.CompositeMeasurement#getMeasurements()
	 * @see #getCompositeMeasurement()
	 * @generated
	 */
	public EReference getCompositeMeasurement_Measurements() {
		return (EReference)compositeMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.RelativeSpeed <em>Relative Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Relative Speed</em>'.
	 * @see net.sf.seesea.model.core.physx.RelativeSpeed
	 * @generated
	 */
	public EClass getRelativeSpeed() {
		return relativeSpeedEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.RelativeSpeed#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see net.sf.seesea.model.core.physx.RelativeSpeed#getKey()
	 * @see #getRelativeSpeed()
	 * @generated
	 */
	public EAttribute getRelativeSpeed_Key() {
		return (EAttribute)relativeSpeedEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the reference '{@link net.sf.seesea.model.core.physx.RelativeSpeed#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see net.sf.seesea.model.core.physx.RelativeSpeed#getValue()
	 * @see #getRelativeSpeed()
	 * @generated
	 */
	public EReference getRelativeSpeed_Value() {
		return (EReference)relativeSpeedEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link net.sf.seesea.model.core.physx.Acceleration <em>Acceleration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Acceleration</em>'.
	 * @see net.sf.seesea.model.core.physx.Acceleration
	 * @generated
	 */
	public EClass getAcceleration() {
		return accelerationEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Acceleration#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see net.sf.seesea.model.core.physx.Acceleration#getX()
	 * @see #getAcceleration()
	 * @generated
	 */
	public EAttribute getAcceleration_X() {
		return (EAttribute)accelerationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Acceleration#getY <em>Y</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see net.sf.seesea.model.core.physx.Acceleration#getY()
	 * @see #getAcceleration()
	 * @generated
	 */
	public EAttribute getAcceleration_Y() {
		return (EAttribute)accelerationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the attribute '{@link net.sf.seesea.model.core.physx.Acceleration#getZ <em>Z</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Z</em>'.
	 * @see net.sf.seesea.model.core.physx.Acceleration#getZ()
	 * @see #getAcceleration()
	 * @generated
	 */
	public EAttribute getAcceleration_Z() {
		return (EAttribute)accelerationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.physx.TemperatureUnit <em>Temperature Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Temperature Unit</em>'.
	 * @see net.sf.seesea.model.core.physx.TemperatureUnit
	 * @generated
	 */
	public EEnum getTemperatureUnit() {
		return temperatureUnitEEnum;
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.physx.HeadingType <em>Heading Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Heading Type</em>'.
	 * @see net.sf.seesea.model.core.physx.HeadingType
	 * @generated
	 */
	public EEnum getHeadingType() {
		return headingTypeEEnum;
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.physx.SpeedUnit <em>Speed Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Speed Unit</em>'.
	 * @see net.sf.seesea.model.core.physx.SpeedUnit
	 * @generated
	 */
	public EEnum getSpeedUnit() {
		return speedUnitEEnum;
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.physx.HandOrientation <em>Hand Orientation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Hand Orientation</em>'.
	 * @see net.sf.seesea.model.core.physx.HandOrientation
	 * @generated
	 */
	public EEnum getHandOrientation() {
		return handOrientationEEnum;
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.physx.LengthUnit <em>Length Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Length Unit</em>'.
	 * @see net.sf.seesea.model.core.physx.LengthUnit
	 * @generated
	 */
	public EEnum getLengthUnit() {
		return lengthUnitEEnum;
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.physx.SpeedType <em>Speed Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Speed Type</em>'.
	 * @see net.sf.seesea.model.core.physx.SpeedType
	 * @generated
	 */
	public EEnum getSpeedType() {
		return speedTypeEEnum;
	}

	/**
	 * Returns the meta object for enum '{@link net.sf.seesea.model.core.physx.DistanceType <em>Distance Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Distance Type</em>'.
	 * @see net.sf.seesea.model.core.physx.DistanceType
	 * @generated
	 */
	public EEnum getDistanceType() {
		return distanceTypeEEnum;
	}

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
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
		CorePackageImpl theCorePackage = (CorePackageImpl)EPackage.Registry.INSTANCE.getEPackage(CorePackageImpl.eNS_URI);

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
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.TemperatureImpl <em>Temperature</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.TemperatureImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getTemperature()
		 * @generated
		 */
		public static final EClass TEMPERATURE = eINSTANCE.getTemperature();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute TEMPERATURE__VALUE = eINSTANCE.getTemperature_Value();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute TEMPERATURE__UNIT = eINSTANCE.getTemperature_Unit();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.SpeedImpl <em>Speed</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.SpeedImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getSpeed()
		 * @generated
		 */
		public static final EClass SPEED = eINSTANCE.getSpeed();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute SPEED__SPEED = eINSTANCE.getSpeed_Speed();

		/**
		 * The meta object literal for the '<em><b>Speed Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute SPEED__SPEED_UNIT = eINSTANCE.getSpeed_SpeedUnit();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.HeadingImpl <em>Heading</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.HeadingImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getHeading()
		 * @generated
		 */
		public static final EClass HEADING = eINSTANCE.getHeading();

		/**
		 * The meta object literal for the '<em><b>Degrees</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute HEADING__DEGREES = eINSTANCE.getHeading_Degrees();

		/**
		 * The meta object literal for the '<em><b>Heading Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute HEADING__HEADING_TYPE = eINSTANCE.getHeading_HeadingType();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.MeasurementImpl <em>Measurement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.MeasurementImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getMeasurement()
		 * @generated
		 */
		public static final EClass MEASUREMENT = eINSTANCE.getMeasurement();

		/**
		 * The meta object literal for the '<em><b>Sensor ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute MEASUREMENT__SENSOR_ID = eINSTANCE.getMeasurement_SensorID();

		/**
		 * The meta object literal for the '<em><b>Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute MEASUREMENT__TIME = eINSTANCE.getMeasurement_Time();

		/**
		 * The meta object literal for the '<em><b>Timezone</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute MEASUREMENT__TIMEZONE = eINSTANCE.getMeasurement_Timezone();

		/**
		 * The meta object literal for the '<em><b>Valid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute MEASUREMENT__VALID = eINSTANCE.getMeasurement_Valid();

		/**
		 * The meta object literal for the '<em><b>Relative</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute MEASUREMENT__RELATIVE = eINSTANCE.getMeasurement_Relative();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.RelativeWindImpl <em>Relative Wind</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.RelativeWindImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getRelativeWind()
		 * @generated
		 */
		public static final EClass RELATIVE_WIND = eINSTANCE.getRelativeWind();

		/**
		 * The meta object literal for the '<em><b>Bow Orientation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute RELATIVE_WIND__BOW_ORIENTATION = eINSTANCE.getRelativeWind_BowOrientation();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.SatelliteInfoImpl <em>Satellite Info</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.SatelliteInfoImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getSatelliteInfo()
		 * @generated
		 */
		public static final EClass SATELLITE_INFO = eINSTANCE.getSatelliteInfo();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute SATELLITE_INFO__ID = eINSTANCE.getSatelliteInfo_Id();

		/**
		 * The meta object literal for the '<em><b>Elevation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute SATELLITE_INFO__ELEVATION = eINSTANCE.getSatelliteInfo_Elevation();

		/**
		 * The meta object literal for the '<em><b>Azimuth</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute SATELLITE_INFO__AZIMUTH = eINSTANCE.getSatelliteInfo_Azimuth();

		/**
		 * The meta object literal for the '<em><b>Signal Strength</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute SATELLITE_INFO__SIGNAL_STRENGTH = eINSTANCE.getSatelliteInfo_SignalStrength();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.SatellitesVisibleImpl <em>Satellites Visible</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.SatellitesVisibleImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getSatellitesVisible()
		 * @generated
		 */
		public static final EClass SATELLITES_VISIBLE = eINSTANCE.getSatellitesVisible();

		/**
		 * The meta object literal for the '<em><b>Satellite Info</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference SATELLITES_VISIBLE__SATELLITE_INFO = eINSTANCE.getSatellitesVisible_SatelliteInfo();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.TimeImpl <em>Time</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.TimeImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getTime()
		 * @generated
		 */
		public static final EClass TIME = eINSTANCE.getTime();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.DistanceImpl <em>Distance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.DistanceImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getDistance()
		 * @generated
		 */
		public static final EClass DISTANCE = eINSTANCE.getDistance();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute DISTANCE__VALUE = eINSTANCE.getDistance_Value();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute DISTANCE__UNIT = eINSTANCE.getDistance_Unit();

		/**
		 * The meta object literal for the '<em><b>Distance Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute DISTANCE__DISTANCE_TYPE = eINSTANCE.getDistance_DistanceType();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.CompositeMeasurementImpl <em>Composite Measurement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.CompositeMeasurementImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getCompositeMeasurement()
		 * @generated
		 */
		public static final EClass COMPOSITE_MEASUREMENT = eINSTANCE.getCompositeMeasurement();

		/**
		 * The meta object literal for the '<em><b>Measurements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference COMPOSITE_MEASUREMENT__MEASUREMENTS = eINSTANCE.getCompositeMeasurement_Measurements();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.RelativeSpeedImpl <em>Relative Speed</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.RelativeSpeedImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getRelativeSpeed()
		 * @generated
		 */
		public static final EClass RELATIVE_SPEED = eINSTANCE.getRelativeSpeed();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute RELATIVE_SPEED__KEY = eINSTANCE.getRelativeSpeed_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference RELATIVE_SPEED__VALUE = eINSTANCE.getRelativeSpeed_Value();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.impl.AccelerationImpl <em>Acceleration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.impl.AccelerationImpl
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getAcceleration()
		 * @generated
		 */
		public static final EClass ACCELERATION = eINSTANCE.getAcceleration();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute ACCELERATION__X = eINSTANCE.getAcceleration_X();

		/**
		 * The meta object literal for the '<em><b>Y</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute ACCELERATION__Y = eINSTANCE.getAcceleration_Y();

		/**
		 * The meta object literal for the '<em><b>Z</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute ACCELERATION__Z = eINSTANCE.getAcceleration_Z();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.TemperatureUnit <em>Temperature Unit</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.TemperatureUnit
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getTemperatureUnit()
		 * @generated
		 */
		public static final EEnum TEMPERATURE_UNIT = eINSTANCE.getTemperatureUnit();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.HeadingType <em>Heading Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.HeadingType
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getHeadingType()
		 * @generated
		 */
		public static final EEnum HEADING_TYPE = eINSTANCE.getHeadingType();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.SpeedUnit <em>Speed Unit</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.SpeedUnit
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getSpeedUnit()
		 * @generated
		 */
		public static final EEnum SPEED_UNIT = eINSTANCE.getSpeedUnit();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.HandOrientation <em>Hand Orientation</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.HandOrientation
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getHandOrientation()
		 * @generated
		 */
		public static final EEnum HAND_ORIENTATION = eINSTANCE.getHandOrientation();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.LengthUnit <em>Length Unit</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.LengthUnit
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getLengthUnit()
		 * @generated
		 */
		public static final EEnum LENGTH_UNIT = eINSTANCE.getLengthUnit();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.SpeedType <em>Speed Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.SpeedType
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getSpeedType()
		 * @generated
		 */
		public static final EEnum SPEED_TYPE = eINSTANCE.getSpeedType();

		/**
		 * The meta object literal for the '{@link net.sf.seesea.model.core.physx.DistanceType <em>Distance Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.seesea.model.core.physx.DistanceType
		 * @see net.sf.seesea.model.core.physx.impl.PhysxPackageImpl#getDistanceType()
		 * @generated
		 */
		public static final EEnum DISTANCE_TYPE = eINSTANCE.getDistanceType();

	}

} //PhysxPackageImpl
