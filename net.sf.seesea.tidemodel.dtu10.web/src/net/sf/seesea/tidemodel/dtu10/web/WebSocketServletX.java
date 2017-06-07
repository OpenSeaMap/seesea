package net.sf.seesea.tidemodel.dtu10.web;

import java.util.Arrays;

import javax.servlet.ServletException;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

@Component
public class WebSocketServletX extends WebSocketServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4424874833269448361L;
	
	@Reference
	protected volatile HttpService httpService;
	
	@Activate
	public void start(ComponentContext cc) throws ServletException, NamespaceException  {
			//Store the current CCL
			ClassLoader ccl = Thread.currentThread().getContextClassLoader();

			//We have to set the CCL to Jetty's bunle classloader
			BundleWiring bundleWiring = findJettyBundle(cc).adapt(BundleWiring.class);
			ClassLoader classLoader = bundleWiring.getClassLoader();
			Thread.currentThread().setContextClassLoader(classLoader);

			httpService.registerServlet("/ws", this, null, null);
			
			//Restore the CCL
			Thread.currentThread().setContextClassLoader(ccl);
			
	}
	
	@Deactivate
	public void stop(ComponentContext cc) {
		httpService.unregister("/ws");
	}
	
	private Bundle findJettyBundle(ComponentContext cc) {
		return Arrays.stream(cc.getBundleContext().getBundles()).filter(b -> b.getSymbolicName().equals("org.apache.felix.http.jetty")).findAny().get();
	}
	
	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(10000);
		factory.register(WebSocketServletX.class);
	}
	
}
