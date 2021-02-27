package io.github.rimonmostafiz.service.project;

import io.github.rimonmostafiz.component.exception.EntityNotFoundException;
import io.github.rimonmostafiz.component.exception.ValidationException;
import io.github.rimonmostafiz.model.dto.ProjectModel;
import io.github.rimonmostafiz.model.entity.activity.ActivityProject;
import io.github.rimonmostafiz.model.entity.common.ActivityAction;
import io.github.rimonmostafiz.model.entity.db.Project;
import io.github.rimonmostafiz.model.entity.db.Task;
import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.model.mapper.ProjectMapper;
import io.github.rimonmostafiz.model.request.ProjectCreateRequest;
import io.github.rimonmostafiz.model.response.ProjectDeleteResponse;
import io.github.rimonmostafiz.repository.ProjectRepository;
import io.github.rimonmostafiz.repository.TaskRepository;
import io.github.rimonmostafiz.repository.activity.ActivityProjectRepository;
import io.github.rimonmostafiz.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Rimon Mostafiz
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    public static final Supplier<EntityNotFoundException> projectNotFound = () ->
            new EntityNotFoundException(HttpStatus.BAD_REQUEST, "projectId", "error.project.not.found");
    public static final Supplier<ValidationException> notOwnProject = () ->
            new ValidationException(HttpStatus.UNAUTHORIZED, "projectId", "error.project.user.not.authorized");
    private final UserService userService;
    private final MessageSource messageSource;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ActivityProjectRepository activityProjectRepository;

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

    public ProjectModel getProjectForUser(Long id, String username) {
        Predicate<Project> isOwnProject = project -> project.getCreatedBy().equals(username);

        return Optional.of(projectRepository.findById(id))
                .orElseThrow(projectNotFound)
                .filter(isOwnProject)
                .map(ProjectMapper::mapper)
                .orElseThrow(notOwnProject);
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

    public List<ProjectModel> getAllProjectsCreatedBy(String username) {
        return projectRepository.findAllByCreatedBy(username)
                .stream()
                .map(ProjectMapper::mapper)
                .collect(Collectors.toList());
    }

    public List<ProjectModel> getAllProjectsByUser(Long userId) {
        List<Project> projects = userService.findById(userId)
                .map(projectRepository::findAllByAssignedUser)
                .orElseThrow(UserService.userNotFound);

        return projects.stream()
                .map(ProjectMapper::mapper)
                .collect(Collectors.toList());
    }

    public ProjectDeleteResponse deleteProject(Long id, String requestUser) {
        final Project project = Optional.ofNullable(findProjectById(id))
                .orElseThrow(projectNotFound);

        final List<Task> tasks = taskRepository.findAllByProject(project);

        if (CollectionUtils.isEmpty(tasks)) {
            final var activityProject = ActivityProject.of(project, requestUser, ActivityAction.DELETE);
            projectRepository.deleteById(id);
            activityProjectRepository.save(activityProject);
        } else {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "projectId", "error.project.assigned.task");
        }
        Locale locale = LocaleContextHolder.getLocale();
        var message = messageSource.getMessage("success.project.delete", new Object[]{id, project.getName()}, locale);
        return ProjectDeleteResponse.of(message, ProjectMapper.mapperForInternal(project));
    }
}
