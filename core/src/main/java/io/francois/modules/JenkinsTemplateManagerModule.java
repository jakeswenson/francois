package io.francois.modules;

import io.francois.ServiceConfiguration;
import io.francois.jenkins.JenkinsTemplateManager;
import io.francois.jenkins.TemplateManager;
import io.francois.jenkins.api.JenkinsApiClient;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class JenkinsTemplateManagerModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    public JenkinsTemplateManager getJenkinsApi(JenkinsApiClient jenkinsApi, ServiceConfiguration configuration) {
        return new TemplateManager(jenkinsApi);
    }
}
