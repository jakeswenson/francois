package io.francois.modules;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import io.francois.ServiceConfiguration;
import io.francois.configurations.JenkinsConfiguration;
import io.francois.jenkins.api.JenkinsApiClient;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class JenkinsApiModule extends AbstractModule {

    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper =
                new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                                  .configure(SerializationFeature.INDENT_OUTPUT, true)
                                  .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true)
                                  .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                                  .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
                                  .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
                                  .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                                  .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                  .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        mapper.registerModule(new JodaModule());

        return mapper;
    }

    @Override protected void configure() {
    }

    @Provides
    public JenkinsApiClient getJenkinsApiClient(ServiceConfiguration configuration, ObjectMapper objectMapper) {
        final JenkinsConfiguration jenkinsConfiguration = configuration.getJenkinsConfiguration();
        return JenkinsApiClient.createClient(
                jenkinsConfiguration.getUrl(),
                jenkinsConfiguration.getUser(),
                jenkinsConfiguration.getToken(),
                objectMapper);
    }

    @Provides
    public ObjectMapper createConfiguredObjectMapper() {
        return createObjectMapper();
    }
}
