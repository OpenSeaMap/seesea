package net.sf.seesea.tidemodel.dtu10.web;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Component(service = Servlet.class, property = "osgi.http.whiteboard.servlet.pattern=/tides", scope = ServiceScope.PROTOTYPE)
public class DTUTideService extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6602908531038565496L;

	@GET
	public double getTideHeight() {
		return 5.5;
	}
	
	
}
