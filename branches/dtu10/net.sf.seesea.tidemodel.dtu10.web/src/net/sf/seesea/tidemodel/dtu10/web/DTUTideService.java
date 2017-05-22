package net.sf.seesea.tidemodel.dtu10.web;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import net.sf.seesea.waterlevel.ocean.ITideProvider;
import net.sf.seesea.waterlevel.ocean.TideCalculationException;

@Component(service = DTUTideService.class)
@Path("/tides")
public class DTUTideService {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public TideHeight getTideHeight() {
		ITideProvider tideProvider = _tideProvider.get();
		TideHeight tideHeight = new TideHeight();
		try {
			double tideHeight2 = tideProvider.getTideHeight(54.5, 7.2, Calendar.getInstance().getTime());
			tideHeight.setHeight(tideHeight2);
		} catch (TideCalculationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tideHeight;
	}
	
	private AtomicReference<ITideProvider> _tideProvider = new AtomicReference<ITideProvider>();
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	public void bindTideService(ITideProvider tideProvider) {
		_tideProvider.set(tideProvider);
	}
	
	public void unbindTideService(ITideProvider tideProvider) {
		_tideProvider.compareAndSet(tideProvider, null);
	}
	
}
