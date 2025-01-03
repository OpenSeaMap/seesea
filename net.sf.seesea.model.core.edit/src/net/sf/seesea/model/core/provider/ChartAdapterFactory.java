/**
 * 
Copyright (c) 2010-2012, Jens K�bler
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

 * 
 */
package net.sf.seesea.model.core.provider;

import net.sf.seesea.model.core.data.provider.DataItemProviderAdapterFactory;
import net.sf.seesea.model.core.geo.osm.provider.OsmItemProviderAdapterFactory;
import net.sf.seesea.model.core.geo.provider.GeoItemProviderAdapterFactory;
import net.sf.seesea.model.core.physx.provider.PhysxItemProviderAdapterFactory;
import net.sf.seesea.model.core.weather.provider.WeatherItemProviderAdapterFactory;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

/**
 * 
 */
public class ChartAdapterFactory extends ComposedAdapterFactory {
	
	/**
	 * 
	 */
	public ChartAdapterFactory() {
		super(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		addAdapterFactory(new ResourceItemProviderAdapterFactory());
		addAdapterFactory(new OsmItemProviderAdapterFactory());
		addAdapterFactory(new GeoItemProviderAdapterFactory());
		addAdapterFactory(new CoreItemProviderAdapterFactory());
		addAdapterFactory(new PhysxItemProviderAdapterFactory());
		addAdapterFactory(new WeatherItemProviderAdapterFactory());
		addAdapterFactory(new DataItemProviderAdapterFactory());
		addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
	}

}
