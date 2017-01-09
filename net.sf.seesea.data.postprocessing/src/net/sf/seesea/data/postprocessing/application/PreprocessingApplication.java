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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

import net.sf.seesea.data.postprocessing.DataPostprocessingActivator;
import net.sf.seesea.data.postprocessing.database.IUploadedData2Contours;

/**
 * This class controls all aspects of the application's execution
 */
public class PreprocessingApplication implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws FileNotFoundException  {
		Logger.getLogger(getClass()).info("Application started");
		Logger.getLogger(getClass()).info("Redirecting stderr to file err.txt");
		
		File file = new File("err.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setErr(ps);
		
		BundleContext bundleContext = DataPostprocessingActivator.getContext();
		
		// this receives the service either through lookup or through a service event whatever comes first
		CompletableFuture<ServiceEvent> serviceEventFuture = new CompletableFuture<ServiceEvent>();
		bundleContext.addServiceListener(new ServiceListenerCompletableFuture(serviceEventFuture));
		CompletableFuture<IUploadedData2Contours> serviceLookup = getServiceByLookup(bundleContext);
		CompletableFuture<IUploadedData2Contours> serviceEvent = serviceEventFuture.thenCompose((result) -> getServiceByEvent(bundleContext, result));
		
		CompletableFuture<IUploadedData2Contours> result = serviceLookup.thenCompose(b 
				-> { 
					if(b == null) 
					{
						return serviceEvent;
					}
					else 
					{
						return CompletableFuture.completedFuture(b);
					}
		});
		// since declarative services 1.2 does not allow to configure required references, we wait for services to come up
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			Logger.getLogger(getClass()).info("Commencing processing");
			IUploadedData2Contours uploadedData2Contours = result.get(120, TimeUnit.SECONDS);
			uploadedData2Contours.processData();
		} catch (InterruptedException | ExecutionException | TimeoutException e ) {
			Logger.getLogger(getClass()).error("Failed to start processing in time. Some service seem not to have started.", e);
		}
		Logger.getLogger(getClass()).info("Processing complete");
		return 0;
	}

	private CompletableFuture<IUploadedData2Contours> getServiceByLookup(BundleContext bundleContext) {
		return CompletableFuture.supplyAsync(() -> { 
			ServiceReference<IUploadedData2Contours> serviceReference = bundleContext.getServiceReference(IUploadedData2Contours.class);
			if(serviceReference != null) {
				IUploadedData2Contours uploadedData2Contours = (IUploadedData2Contours) bundleContext.getService(serviceReference);
				System.out.println("lookup");
				return uploadedData2Contours;
			}
			return null;
//			throw new IllegalArgumentException("No service");
			});
	}

	private CompletableFuture<IUploadedData2Contours> getServiceByEvent(BundleContext bundleContext, ServiceEvent event) {
		return CompletableFuture.supplyAsync(() -> { 
			IUploadedData2Contours uploadedData2Contours = (IUploadedData2Contours) bundleContext.getService(event.getServiceReference());
			return uploadedData2Contours;
			});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		// nothing to do
	}

}
