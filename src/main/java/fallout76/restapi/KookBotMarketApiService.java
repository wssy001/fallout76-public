package fallout76.restapi;

import fallout76.config.CustomKookBotMarketHeaderFactory;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterClientHeaders(CustomKookBotMarketHeaderFactory.class)
public interface KookBotMarketApiService {

    @GET
    @Path("online.bot")
    Response IamOnline();

}
