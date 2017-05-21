package net.sf.seesea.tidemodel.dtu10.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;

@Component
@Path("/tides")
@Produces({ MediaType.APPLICATION_JSON })
public class DTUTideService {

	@GET
	public double getTideHeight() {
		return 5.5;
	}
	
	
}
