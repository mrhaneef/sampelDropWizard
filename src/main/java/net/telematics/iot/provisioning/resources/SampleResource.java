package net.telematics.iot.provisioning.resources;


import com.codahale.metrics.annotation.Timed;
import net.telematics.iot.provisioning.api.Saying;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;


@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)

public class SampleResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private final Client client;

    public SampleResource(String template, String defaultName,Client httpClient) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
        this.client = httpClient;
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));

        WebTarget webTarget = client.target("http://example.com/employees");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        @SuppressWarnings("rawtypes")
        String employees = response.readEntity(String.class);


        return new Saying(counter.incrementAndGet(), employees);
    }
}
