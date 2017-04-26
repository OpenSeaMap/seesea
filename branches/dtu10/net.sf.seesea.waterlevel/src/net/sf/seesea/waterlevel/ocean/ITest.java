package net.sf.seesea.waterlevel.ocean;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/helloworld")
public interface ITest {

    @GET
    @Produces("text/plain")
    public String getMessage();
}
