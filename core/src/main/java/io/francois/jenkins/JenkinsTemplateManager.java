package io.francois.jenkins;

import io.francois.jenkins.templates.JobParameterValue;
import io.francois.jenkins.templates.JobTemplate;
import io.francois.jenkins.templates.TemplateParameter;

import java.io.IOException;
import java.util.List;

public interface JenkinsTemplateManager {
    List<TemplateParameter> getAllParameters(final String templateName) throws IOException;

    JobTemplate getJobTemplate(final String jobTemplateName) throws IOException;

    void createJobFromTemplate(String newJobName, String templateName, List<JobParameterValue> parameterValues) throws Exception;

    List<JobApplicationModel> getTemplatizedJobs(String templateName) throws IOException, Exception;

    void reapplyTemplate(String templateName) throws Exception;
}
