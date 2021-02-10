package io.github.rimonmostafiz.service.project;

import io.github.rimonmostafiz.component.exception.EntityNotFoundException;
import io.github.rimonmostafiz.entity.activity.ActivityProject;
import io.github.rimonmostafiz.entity.common.ActivityAction;
import io.github.rimonmostafiz.entity.common.ActivityCommon;
import io.github.rimonmostafiz.entity.db.Project;
import io.github.rimonmostafiz.model.ProjectModel;
import io.github.rimonmostafiz.repository.ProjectRepository;
import io.github.rimonmostafiz.repository.UserRepository;
import io.github.rimonmostafiz.repository.activity.ActivityProjectRepository;
import io.github.rimonmostafiz.utils.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Rimon Mostafiz
 */
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ActivityProjectRepository activityProjectRepository;

    private final Supplier<EntityNotFoundException> projectNotFound = () ->
            new EntityNotFoundException(HttpStatus.NO_CONTENT, "id", "No project found");

    Supplier<EntityNotFoundException> userNotFound = () ->
            new EntityNotFoundException(HttpStatus.NO_CONTENT, "id", "No user found");

    public ProjectModel createProject(ProjectModel model, String requestUser) {
        Project project = ProjectMapper.modelToEntityMapperForCreate(model, requestUser);
        Project savedProject = projectRepository.save(project);

        ActivityProject activityProject = new ActivityProject();
        ActivityCommon.mapper(activityProject, project);
        ActivityCommon.mapper(activityProject, requestUser, ActivityAction.INSERT);
        activityProjectRepository.save(activityProject);

        return ProjectMapper.mapper(savedProject);
    }

    public ProjectModel getProject(Long id) {
        return projectRepository.findById(id)
                .map(ProjectMapper::mapper)
                .orElseThrow(projectNotFound);
    }

    public List<ProjectModel> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectMapper::mapper)
                .collect(Collectors.toList());
    }

    public List<ProjectModel> getAllProjects(Long userId) {
        List<Project> projects = userRepository.findById(userId)
                .map(projectRepository::findAllByAssignedUser)
                .orElseThrow(userNotFound);

        return projects.stream()
                .map(ProjectMapper::mapper)
                .collect(Collectors.toList());
    }

    public void deleteProject(Long id, String requestUser) {
        Function<Project, ActivityProject> mapToActivity = project ->
                new ActivityProject(project, requestUser, ActivityAction.DELETE);

        ActivityProject activityProject = projectRepository.findById(id)
                .map(mapToActivity)
                .orElseThrow(projectNotFound);

        projectRepository.deleteById(id);
        activityProjectRepository.save(activityProject);
    }
}
