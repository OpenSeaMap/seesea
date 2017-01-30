package net.sf.seesea.provider.navigation.adm;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ADMActivator implements BundleActivator {

	private static BundleContext context;

	@Override
	public void start(BundleContext context) throws Exception {
		ADMActivator.context = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		ADMActivator.context = null;		
	}

	public static BundleContext getContext() {
		return context;
	}



}
