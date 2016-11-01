/**
Copyright (c) 2013-2015, Jens Kübler
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
package net.sf.seesea.data.postprocessing.application;

import java.util.concurrent.CompletableFuture;

import org.apache.log4j.Logger;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import net.sf.seesea.data.postprocessing.database.IUploadedData2Contours;

/**
 * 
 *
 */
final class ServiceListenerCompletableFuture implements ServiceListener {
	
	private Logger logger = Logger.getLogger(ServiceListenerCompletableFuture.class); 
	
	private CompletableFuture<ServiceEvent> serviceEventFuture;
	
	public ServiceListenerCompletableFuture(CompletableFuture<ServiceEvent> future) {
		serviceEventFuture = future;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		if(event.getServiceReference() != null) {
			ServiceReference<?> serviceReference = event.getServiceReference();
			if(serviceReference.getClass().equals(IUploadedData2Contours.class)) {
				serviceEventFuture.complete(event);
				if(logger.isDebugEnabled()) {
					logger.debug("Supply service trhough Service Event:" + event);
				}
			}
		}
	}
}