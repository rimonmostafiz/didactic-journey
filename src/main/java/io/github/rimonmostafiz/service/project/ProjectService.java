package io.github.rimonmostafiz.service.project;

import io.github.rimonmostafiz.component.exception.EntityNotFoundException;
import io.github.rimonmostafiz.model.dto.ProjectModel;
import io.github.rimonmostafiz.model.entity.activity.ActivityProject;
import io.github.rimonmostafiz.model.entity.common.ActivityAction;
import io.github.rimonmostafiz.model.entity.db.Project;
import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.model.mapper.ProjectMapper;
import io.github.rimonmostafiz.model.request.ProjectCreateRequest;
import io.github.rimonmostafiz.repository.ProjectRepository;
import io.github.rimonmostafiz.repository.activity.ActivityProjectRepository;
import io.github.rimonmostafiz.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Rimon Mostafiz
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ActivityProjectRepository activityProjectRepository;

    private final Supplier<EntityNotFoundException> projectNotFound = () ->
            new EntityNotFoundException(HttpStatus.BAD_REQUEST, "projectId", "error.project.not.found");

    Supplier<EntityNotFoundException> userNotFound = () ->
            new EntityNotFoundException(HttpStatus.BAD_REQUEST, "userId", "error.user.not.found");

    public ProjectModel createProject(ProjectCreateRequest createRequest, String requestUser) {
        User user = userService.getUserByUsername(requestUser);
        Project project = ProjectMapper.modelToEntityMapperForCreate(createRequest, user);
        Project savedProject = projectRepository.saveAndFlush(project);

        ActivityProject activityProject = ActivityProject.of(savedProject, requestUser, ActivityAction.INSERT);
        activityProjectRepository.save(activityProject);

        return ProjectMapper.mapper(savedProject);
    }

    public ProjectModel getProject(Long id) {
        return projectRepository.findById(id)
                .map(ProjectMapper::mapper)
                .orElseThrow(projectNotFound);
    }

    public Project findProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(projectNotFound);
    }

    public List<ProjectModel> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectMapper::mapper)
                .collect(Collectors.toList());
    }

    public List<ProjectModel> getAllProjectsByUser(Long userId) {
        List<Project> projects = userService.findById(userId)
                .map(projectRepository::findAllByAssignedUser)
                .orElseThrow(userNotFound);

        return projects.stream()
                .map(ProjectMapper::mapper)
                .collect(Collectors.toList());
    }

    public void deleteProject(Long id, String requestUser) {
        Function<Project, ActivityProject> mapToActivity = project
                -> ActivityProject.of(project, requestUser, ActivityAction.DELETE);

        ActivityProject activityProject = projectRepository.findById(id)
                .map(mapToActivity)
                .orElseThrow(projectNotFound);

        projectRepository.deleteById(id);
        activityProjectRepository.save(activityProject);
    }
}
