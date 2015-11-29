package com.godaddy.domains.unittests;

import static org.assertj.core.api.Assertions.assertThat;

import io.francois.jenkins.api.JenkinsApiClient;
import io.francois.jenkins.api.JobList;
import io.francois.jenkins.api.JobModel;
import org.junit.Test;
import retrofit.Call;
import retrofit.Response;

import java.io.IOException;
import java.util.List;

public class JenkinsApiTests {
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
    }

}