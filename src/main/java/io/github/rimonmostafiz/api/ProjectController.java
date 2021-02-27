package io.github.rimonmostafiz.api;

import io.github.rimonmostafiz.model.common.RestResponse;
import io.github.rimonmostafiz.model.dto.ProjectModel;
import io.github.rimonmostafiz.model.request.ProjectCreateRequest;
import io.github.rimonmostafiz.model.response.ProjectResponse;
import io.github.rimonmostafiz.service.project.ProjectService;
import io.github.rimonmostafiz.utils.ResponseUtils;
import io.github.rimonmostafiz.utils.RoleUtils;
import io.github.rimonmostafiz.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@Api(tags = "Project")
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(value = "/project")
    @ApiOperation(value = "Create Project", code = 201)
    public ResponseEntity<RestResponse<ProjectResponse>> createProject(HttpServletRequest request,
                                                                       @Valid @RequestBody ProjectCreateRequest createRequest) {
        String requestUser = Utils.getUserNameFromRequest(request);
        ProjectModel project = projectService.createProject(createRequest, requestUser);
        ProjectResponse projectResponse = ProjectResponse.of(project);
        return ResponseUtils.buildSuccessResponse(HttpStatus.CREATED, projectResponse);
    }

    @GetMapping("/projects")
    public ResponseEntity<RestResponse<ProjectResponse>> getAllProjects(HttpServletRequest request) {
        List<ProjectModel> projects;
        final String username = Utils.getUserNameFromRequest(request);
        final boolean isAdmin = RoleUtils.hasPrivilege(request, RoleUtils.ADMIN_ROLE);
        projects = isAdmin ? projectService.getAllProjects() : projectService.getAllProjectsCreatedBy(username);
        ProjectResponse projectResponse = ProjectResponse.of(projects);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, projectResponse);
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<RestResponse<Long>> deleteProject(HttpServletRequest request, @PathVariable Long id) {
        String requestUser = Utils.getUserNameFromRequest(request);
        projectService.deleteProject(id, requestUser);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, id);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<RestResponse<ProjectResponse>> getProject(HttpServletRequest request, @PathVariable Long id) {
        ProjectModel project;
        final String username = Utils.getUserNameFromRequest(request);
        final boolean isAdmin = RoleUtils.hasPrivilege(request, RoleUtils.ADMIN_ROLE);
        project = isAdmin ? projectService.getProject(id) : projectService.getProjectUser(id, username);
        ProjectResponse projectResponse = ProjectResponse.of(project);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, projectResponse);
    }

    @GetMapping("/project/search/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(
            value = "Get all projects by user",
            notes = "An ADMIN User has the privilege to call this API"
    )
    public ResponseEntity<RestResponse<ProjectResponse>> getProjectByUserId(@PathVariable Long userId) {
        List<ProjectModel> projects = projectService.getAllProjectsByUser(userId);
        ProjectResponse projectResponse = ProjectResponse.of(projects);
        return ResponseUtils.buildSuccessResponse(HttpStatus.OK, projectResponse);
    }
}
