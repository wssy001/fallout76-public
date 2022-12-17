package fallout76.restapi;

import fallout76.config.CustomHeaderFactory;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RegisterClientHeaders(CustomHeaderFactory.class)
@RegisterRestClient(baseUri = "https://a.roguetrader.com")
public interface NukaCodeApiService {

    @POST
    @Path("/graphql")
    Response getNukaCode(String body);

}
