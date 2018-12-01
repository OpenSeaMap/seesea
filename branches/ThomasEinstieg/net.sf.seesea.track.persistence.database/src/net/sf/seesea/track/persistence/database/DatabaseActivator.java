package net.sf.seesea.track.persistence.database;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class DatabaseActivator implements BundleActivator {

	private static BundleContext context;

	@Override
	public void start(BundleContext context) throws Exception {
		DatabaseActivator.context = context;

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DatabaseActivator.context = null;
	}

	public static BundleContext getContext() {
		return context;
	}
	
	

}
