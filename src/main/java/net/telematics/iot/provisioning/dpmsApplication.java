package net.telematics.iot.provisioning;

import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.telematics.iot.provisioning.health.TemplateHealthCheck;
import net.telematics.iot.provisioning.resources.SampleResource;
import org.apache.http.impl.client.CloseableHttpClient;
import sun.net.www.http.HttpClient;

import javax.ws.rs.client.Client;

public class dpmsApplication extends Application<dpmsConfiguration> {

    public static void main(final String[] args) throws Exception {
        new dpmsApplication().run(args);
    }

    @Override
    public String getName() {
        return "dpms";
    }

    @Override
    public void initialize(final Bootstrap<dpmsConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final dpmsConfiguration configuration,
                    final Environment environment) {

        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());


        final Client client = new JerseyClientBuilder(environment).build("DemoRESTClient");

        SampleResource resource = new SampleResource(configuration.getTemplate(),configuration.getDefaultName(),client);


        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }

}
