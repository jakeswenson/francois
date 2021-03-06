package io.francois.jenkins.templates;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor(onConstructor = @_(@JsonCreator))
public class JobDefinition {
    @NonNull
    JobTemplate template;

    @NonNull
    List<JobParameterValue> parameterValues;
}
