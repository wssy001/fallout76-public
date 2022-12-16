package fallout76.restapi;

import com.fasterxml.jackson.databind.JsonNode;
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
@RegisterClientHeaders(CustomHeaderFactory.class)
public interface GoCQHttpApiService {

    @POST
    @Path("/send_group_msg")
    @Produces(MediaType.APPLICATION_JSON)
    Response sendGroupMessage(String body);

    @POST
    @Path("/send_group_msg")
    @Produces(MediaType.APPLICATION_JSON)
    JsonNode testSendGroupMessage(String body);

    @POST
    @Path("/send_private_msg")
    @Produces(MediaType.APPLICATION_JSON)
    Response sendPrivateMessage(String body);

    @POST
    @Path("/send_guild_channel_msg")
    @Produces(MediaType.APPLICATION_JSON)
    Response sendGuildChannelMessage(String body);
}
