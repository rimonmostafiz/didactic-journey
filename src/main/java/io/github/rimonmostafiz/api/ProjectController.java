package io.github.rimonmostafiz.api;

import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.model.dto.ProjectModel;
import io.github.rimonmostafiz.model.response.ProjectResponse;
import io.github.rimonmostafiz.service.project.ProjectService;
import io.github.rimonmostafiz.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Rimon Mostafiz
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/project")
    @ResponseStatus
    public ResponseEntity<RestResponse<ProjectResponse>> addProject(HttpServletRequest request, ProjectModel model) {
        //TODO: need to update after enabling spring security
        String requestUser = "user";
        //String requestUser = Utils.getUserNameFromRequest(request);
        ProjectModel project = projectService.createProject(model, requestUser);
        ProjectResponse projectResponse = ProjectResponse.of(project);
        return ResponseUtils.buildSuccessResponse(HttpStatus.CREATED, projectResponse);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<RestResponse<ProjectResponse>> getProject(@PathVariable Long id) {
        ProjectModel project = projectService.getProject(id);
        ProjectResponse projectResponse = ProjectResponse.of(project);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, projectResponse);
    }

    @GetMapping("/project/search/{userId}")
    public ResponseEntity<RestResponse<ProjectResponse>> getProjectByUserId(@PathVariable Long userId) {
        List<ProjectModel> projects = projectService.getAllProjectsByUser(userId);
        ProjectResponse projectResponse = ProjectResponse.of(projects);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, projectResponse);
    }

    @GetMapping("/projects")
    public ResponseEntity<RestResponse<ProjectResponse>> getProjects() {
        List<ProjectModel> projects = projectService.getAllProjects();
        ProjectResponse projectResponse = ProjectResponse.of(projects);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, projectResponse);
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<RestResponse<Long>> deleteProject(HttpServletRequest request, @PathVariable Long id) {
        //TODO: need to update after enabling spring security
        String requestUser = "user";
        //String requestUser = Utils.getUserNameFromRequest(request);
        projectService.deleteProject(id, requestUser);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, id);
    }
}
