/**
 * Copyright (c) 2010-2018, Jens Kuebler
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package net.sf.seesea.model.core.geo.impl;

import java.util.Date;
import net.sf.seesea.model.core.geo.EstimatedPosition;
import net.sf.seesea.model.core.geo.GeoPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Estimated Position</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.sf.seesea.model.core.geo.impl.EstimatedPositionImpl#getLatVariance <em>Lat Variance</em>}</li>
 *   <li>{@link net.sf.seesea.model.core.geo.impl.EstimatedPositionImpl#getLonVariance <em>Lon Variance</em>}</li>
 *   <li>{@link net.sf.seesea.model.core.geo.impl.EstimatedPositionImpl#getTime <em>Time</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EstimatedPositionImpl extends GeoPositionImpl implements EstimatedPosition {
	/**
	 * The default value of the '{@link #getLatVariance() <em>Lat Variance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatVariance()
	 * @generated
	 * @ordered
	 */
	protected static final double LAT_VARIANCE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLatVariance() <em>Lat Variance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatVariance()
	 * @generated
	 * @ordered
	 */
	protected double latVariance = LAT_VARIANCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLonVariance() <em>Lon Variance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLonVariance()
	 * @generated
	 * @ordered
	 */
	protected static final double LON_VARIANCE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLonVariance() <em>Lon Variance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLonVariance()
	 * @generated
	 * @ordered
	 */
	protected double lonVariance = LON_VARIANCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTime() <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTime()
	 * @generated
	 * @ordered
	 */
	protected static final Date TIME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTime() <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTime()
	 * @generated
	 * @ordered
	 */
	protected Date time = TIME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EstimatedPositionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GeoPackage.Literals.ESTIMATED_POSITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getLatVariance() {
		return latVariance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLatVariance(double newLatVariance) {
		double oldLatVariance = latVariance;
		latVariance = newLatVariance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GeoPackage.ESTIMATED_POSITION__LAT_VARIANCE, oldLatVariance, latVariance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getLonVariance() {
		return lonVariance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLonVariance(double newLonVariance) {
		double oldLonVariance = lonVariance;
		lonVariance = newLonVariance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GeoPackage.ESTIMATED_POSITION__LON_VARIANCE, oldLonVariance, lonVariance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTime(Date newTime) {
		Date oldTime = time;
		time = newTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GeoPackage.ESTIMATED_POSITION__TIME, oldTime, time));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GeoPackage.ESTIMATED_POSITION__LAT_VARIANCE:
				return getLatVariance();
			case GeoPackage.ESTIMATED_POSITION__LON_VARIANCE:
				return getLonVariance();
			case GeoPackage.ESTIMATED_POSITION__TIME:
				return getTime();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GeoPackage.ESTIMATED_POSITION__LAT_VARIANCE:
				setLatVariance((Double)newValue);
				return;
			case GeoPackage.ESTIMATED_POSITION__LON_VARIANCE:
				setLonVariance((Double)newValue);
				return;
			case GeoPackage.ESTIMATED_POSITION__TIME:
				setTime((Date)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case GeoPackage.ESTIMATED_POSITION__LAT_VARIANCE:
				setLatVariance(LAT_VARIANCE_EDEFAULT);
				return;
			case GeoPackage.ESTIMATED_POSITION__LON_VARIANCE:
				setLonVariance(LON_VARIANCE_EDEFAULT);
				return;
			case GeoPackage.ESTIMATED_POSITION__TIME:
				setTime(TIME_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case GeoPackage.ESTIMATED_POSITION__LAT_VARIANCE:
				return latVariance != LAT_VARIANCE_EDEFAULT;
			case GeoPackage.ESTIMATED_POSITION__LON_VARIANCE:
				return lonVariance != LON_VARIANCE_EDEFAULT;
			case GeoPackage.ESTIMATED_POSITION__TIME:
				return TIME_EDEFAULT == null ? time != null : !TIME_EDEFAULT.equals(time);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (latVariance: "); //$NON-NLS-1$
		result.append(latVariance);
		result.append(", lonVariance: "); //$NON-NLS-1$
		result.append(lonVariance);
		result.append(", time: "); //$NON-NLS-1$
		result.append(time);
		result.append(')');
		return result.toString();
	}

} //EstimatedPositionImpl
