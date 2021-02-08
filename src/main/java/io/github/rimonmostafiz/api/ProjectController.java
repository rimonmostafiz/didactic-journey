package io.github.rimonmostafiz.api;

import io.github.rimonmostafiz.io.github.rimonmostafiz.service.project.ProjectService;
import io.github.rimonmostafiz.model.ProjectModel;
import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.model.response.ProjectResponse;
import io.github.rimonmostafiz.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rimon Mostafiz
 */
@RestController
@RequestMapping("/task-manager-api")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/project")
    public ResponseEntity<RestResponse<ProjectResponse>> addProject(HttpServletRequest request, ProjectModel model) {
        //TODO: need to update after enabling spring security
        String requestUser = "user";
        //String requestUser = Utils.getUserNameFromRequest(request);
        ProjectResponse response = projectService.createProject(model, requestUser);
        return Utils.buildSuccessResponse(HttpStatus.CREATED, response);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<RestResponse<ProjectResponse>> getProject(@PathVariable Long id) {
        ProjectResponse projectResponse = projectService.getProject(id);
        return Utils.buildSuccessResponse(HttpStatus.OK, projectResponse);
    }

    @GetMapping("/projects")
    public ResponseEntity<RestResponse<ProjectResponse>> getProjects() {
        ProjectResponse projectResponse = projectService.getAllProjects();
        return Utils.buildSuccessResponse(HttpStatus.OK, projectResponse);
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<RestResponse<Long>> deleteProject(HttpServletRequest request, @PathVariable Long id) {
        //TODO: need to update after enabling spring security
        String requestUser = "user";
        //String requestUser = Utils.getUserNameFromRequest(request);
        projectService.deleteProject(id, requestUser);
        return Utils.buildSuccessResponse(HttpStatus.OK, id);
    }
}
