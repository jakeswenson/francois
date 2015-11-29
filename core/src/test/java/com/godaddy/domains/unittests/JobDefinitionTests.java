package com.godaddy.domains.unittests;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.francois.jenkins.api.JenkinsApiClient;
import io.francois.jenkins.api.JobList;
import io.francois.jenkins.api.JobModel;
import io.francois.jenkins.templates.JobTemplate;
import com.google.common.collect.ImmutableMap;
import com.squareup.okhttp.ResponseBody;
import org.junit.Ignore;
import org.junit.Test;
import retrofit.Call;
import retrofit.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JobDefinitionTests {
    @Test
    public void test_does_work() throws IOException {
        final JenkinsApiClient client = JenkinsApiClient.createClient("http://dom-jenkins.cloud.dev.phx3.gdg/", "francois", "f58671c2c594ba64b90c0a086f7a98bb");

        final Call<JobList> jobTemplates = client.getJobTemplates();

        final JobList body = jobTemplates.execute().body();
        final List<JobModel> jobs = body.getJobs();

        assertThat(jobs).isNotEmpty();

        final JobModel jobListItem = jobs.get(0);

        final Call<String> jobDefinition = client.getJobConfig(jobListItem.getName());

        final Response<String> response = jobDefinition.execute();

        final String xml = response.body();

        assertThat(xml).isNotNull();

        final JobTemplate jobTemplate1 = new JobTemplate(jobListItem.getName(), xml);
        final HashMap<String, String> parameters = jobTemplate1.getParameters();

        assertThat(parameters).isNotEmpty();
    }

    @Test
    @Ignore("Actually Creates a job")
    public void TestTemplateCreate() throws IOException {
        final JenkinsApiClient client = JenkinsApiClient.createClient("http://dom-jenkins.cloud.dev.phx3.gdg/", "francois", "f58671c2c594ba64b90c0a086f7a98bb");

        final Call<JobList> jobTemplates = client.getJobTemplates();

        final JobList body = jobTemplates.execute().body();
        final List<JobModel> jobs = body.getJobs();

        assertThat(jobs).isNotEmpty();

        final JobModel jobListItem = jobs.get(0);

        final Call<String> jobDefinition = client.getJobConfig(jobListItem.getName());

        final Response<String> response = jobDefinition.execute();

        final String xml = response.body();

        assertThat(xml).isNotNull();

        final JobTemplate jobTemplate1 = new JobTemplate(jobListItem.getName(), xml);
        final HashMap<String, String> parameters = jobTemplate1.getParameters();

        assertThat(parameters).isNotEmpty();

        final String config = jobTemplate1.apply(Collections.emptyMap());

        final Call<ResponseBody> newJob = client.createJob("JakeTest", config);

        final Response<ResponseBody> execute = newJob.execute();
    }

    @Test
    public void TestThingsWork() throws JsonProcessingException {
        final JobTemplate test = new JobTemplate("test", "{% A %} {% B | %} {% B | C %}");

        final HashMap<String, String> parameters = test.getParameters();

        assertThat(parameters).hasSize(2);

        final String config = test.apply(ImmutableMap.of("A", "TEST"));

        assertThat(config).isEqualTo("TEST C C");
    }

}
