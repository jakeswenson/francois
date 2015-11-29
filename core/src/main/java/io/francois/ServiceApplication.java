package io.francois;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.reader.ClassReaders;
import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.jackson.JacksonMessageBodyProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.views.ViewRenderer;
import io.dropwizard.views.mustache.MustacheViewRenderer;
import io.francois.modules.DataAccessModule;
import io.francois.modules.JenkinsApiModule;
import io.francois.modules.JenkinsTemplateManagerModule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ServiceApplication extends Application<ServiceConfiguration> {

    public static void main(String[] args) throws Exception {

        try {
            final ServiceApplication app = new ServiceApplication();
            app.run(args);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new TemplateConfigBundle());

        initializeViews(bootstrap);

        initializeDepedencyInjection(bootstrap);
    }

    private void initializeViews(final Bootstrap<ServiceConfiguration> bootstrap) {
        List<ViewRenderer> viewRenders = new ArrayList<>();

        viewRenders.add(new MustacheViewRenderer());

        bootstrap.addBundle(new ViewBundle<>(viewRenders));

        bootstrap.addBundle(new AssetsBundle("/assets", "/assets"));
    }

    private void initializeDepedencyInjection(final Bootstrap<ServiceConfiguration> bootstrap) {
        GuiceBundle<ServiceConfiguration> guiceBundle =
                GuiceBundle.<ServiceConfiguration>newBuilder()
                        .addModule(new DataAccessModule())
                        .addModule(new JenkinsApiModule())
                        .addModule(new JenkinsTemplateManagerModule())
                        .enableAutoConfig(getClass().getPackage().getName())
                        .setConfigClass(ServiceConfiguration.class)
                        .build();

        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(ServiceConfiguration config, final Environment env) throws Exception {
        ArrayList<BiConsumer<ServiceConfiguration, Environment>> run = new ArrayList<>();

        run.add(this::configureJson);

        run.add(this::configureDataAccess);

        run.add(this::configureDiscoverableApiHelp);

        run.add(this::configureLogging);

        run.stream().forEach(i -> i.accept(config, env));
    }

    private void configureLogging(final ServiceConfiguration serviceConfiguration, final Environment environment) {

    }

    private void configureDiscoverableApiHelp(
            final ServiceConfiguration config,
            final Environment environment) {

        environment.jersey().register(new ApiListingResourceJSON());
        environment.jersey().register(new ResourceListingProvider());
        environment.jersey().register(new ApiDeclarationProvider());

        ScannerFactory.setScanner(new DefaultJaxrsScanner());

        ClassReaders.setReader(new DefaultJaxrsApiReader());

        SwaggerConfig swagConfig = ConfigFactory.config();

        swagConfig.setApiVersion("1.0.1");

        swagConfig.setBasePath(environment.getApplicationContext().getContextPath());

        ApiInfo info = new ApiInfo(
                "francois API",                                   /* title */
                "francois API",
                "http://github.com/jakeswenson/francois",         /* TOS URL */
                "@jakeswenson",                                   /* Contact */
                "Apache 2.0",                                     /* license */
                "http://www.apache.org/licenses/LICENSE-2.0.html" /* license URL */
        );

        swagConfig.setApiInfo(info);
    }

    private void configureDataAccess(final ServiceConfiguration serviceConfiguration, final Environment environment) {

    }

    private void configureJson(ServiceConfiguration config, final Environment environment) {
        JacksonMessageBodyProvider jacksonBodyProvider =
                new JacksonMessageBodyProvider(JenkinsApiModule.createObjectMapper(), environment.getValidator());

        environment.jersey().register(jacksonBodyProvider);
    }


}
