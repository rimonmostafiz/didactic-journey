package io.github.rimonmostafiz.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.rimonmostafiz.model.dto.ProjectModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rimon Mostafiz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDeleteResponse {
    private String message;

    private ProjectModel project;
}
