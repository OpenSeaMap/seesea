package net.sf.seesea.tidemodel.dtu10.web;

import javax.servlet.http.HttpServlet;

import org.osgi.service.component.annotations.Component;

@Component(service = MyResourceService.class , 
property= {"osgi.http.whiteboard.resource.pattern=/static/*",
           "osgi.http.whiteboard.resource.prefix=/static"})
public class MyResourceService extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2635187916827113774L;

}
