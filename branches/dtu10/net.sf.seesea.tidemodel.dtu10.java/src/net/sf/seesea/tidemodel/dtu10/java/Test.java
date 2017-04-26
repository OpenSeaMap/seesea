package net.sf.seesea.tidemodel.dtu10.java;

import org.osgi.service.component.annotations.Component;

import net.sf.seesea.waterlevel.ocean.ITest;

@Component(property = {"service.exported.interfaces=*","service.exported.configs=ecf.jaxrs.jersey.server", "ecf.jaxrs.jersey.server.alias=/jersey"})
public class Test implements ITest{

	@Override
	public String getMessage() {
		return "hallo";
	}

	
}
