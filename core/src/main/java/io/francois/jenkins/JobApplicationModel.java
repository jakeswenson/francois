package io.francois.jenkins;

import io.francois.jenkins.templates.JobTemplateApplicationConfig;
import lombok.Value;

@Value
public class JobApplicationModel {
    private final String jobName;
    private final JobTemplateApplicationConfig config;
}
