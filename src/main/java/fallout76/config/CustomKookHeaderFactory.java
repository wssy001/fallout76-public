package fallout76.config;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

@ApplicationScoped
public class CustomKookHeaderFactory implements ClientHeadersFactory {

    @Inject
    RobotConfig robotConfig;

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36";


    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientOutgoingHeaders) {
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        result.add(HttpHeaders.USER_AGENT, USER_AGENT);

        String token = robotConfig.kook()
                .token()
                .orElse("");
        result.add(HttpHeaders.AUTHORIZATION, "Bot " + token);
        return result;
    }
}
