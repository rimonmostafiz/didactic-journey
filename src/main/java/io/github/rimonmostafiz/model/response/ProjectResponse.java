package io.github.rimonmostafiz.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.rimonmostafiz.model.ProjectModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rimon Mostafiz
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponse {

    private List<ProjectModel> projects;

    private ProjectModel project;

    public static ProjectResponse of(List<ProjectModel> projects) {
        ProjectResponse response = new ProjectResponse();
        response.setProjects(projects);
        return response;
    }

    public static ProjectResponse of(ProjectModel project) {
        ProjectResponse response = new ProjectResponse();
        response.setProject(project);
        return response;
    }
}
