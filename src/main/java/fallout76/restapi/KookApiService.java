package fallout76.restapi;

import fallout76.config.CustomKookHeaderFactory;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("api/v3")
@Consumes(MediaType.APPLICATION_JSON)
@RegisterClientHeaders(CustomKookHeaderFactory.class)
public interface KookApiService {

    @POST
    @Path("message/create")
    @Produces(MediaType.APPLICATION_JSON)
    Response sendGuildMessage(String body);


    @POST
    @Path("user-chat/create")
    @Produces(MediaType.APPLICATION_JSON)
    Response sendGuildPrivateMessage(String body);

}
