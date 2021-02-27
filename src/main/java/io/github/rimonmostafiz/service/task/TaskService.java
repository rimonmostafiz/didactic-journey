package io.github.rimonmostafiz.service.task;

import io.github.rimonmostafiz.component.exception.EntityNotFoundException;
import io.github.rimonmostafiz.component.exception.ValidationException;
import io.github.rimonmostafiz.model.dto.TaskModel;
import io.github.rimonmostafiz.model.entity.activity.ActivityTask;
import io.github.rimonmostafiz.model.entity.common.ActivityAction;
import io.github.rimonmostafiz.model.entity.common.TaskStatus;
import io.github.rimonmostafiz.model.entity.db.Project;
import io.github.rimonmostafiz.model.entity.db.Task;
import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.model.mapper.TaskMapper;
import io.github.rimonmostafiz.model.request.TaskCreateRequest;
import io.github.rimonmostafiz.model.request.TaskEditRequest;
import io.github.rimonmostafiz.repository.TaskRepository;
import io.github.rimonmostafiz.repository.activity.ActivityTaskRepository;
import io.github.rimonmostafiz.service.project.ProjectService;
import io.github.rimonmostafiz.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Rimon Mostafiz
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final UserService userservice;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final ActivityTaskRepository activityTaskRepository;

    public static final Supplier<EntityNotFoundException> taskNotFound = () ->
            new EntityNotFoundException(HttpStatus.BAD_REQUEST, "taskId", "error.task.not.found");

    public static final Supplier<ValidationException> notOwnTask = () ->
            new ValidationException(HttpStatus.UNAUTHORIZED, "taskId", "error.task.user.not.authorized");

    public TaskModel createTask(TaskCreateRequest taskCreateRequest, String requestUser) {
        Project project = projectService.findProjectById(taskCreateRequest.getProjectId());
        User user = userservice.getUserByUsername(requestUser);
        Task task = TaskMapper.createRequestToEntity(taskCreateRequest, requestUser, project, user);
        Task savedTask = taskRepository.save(task);

        ActivityTask activityTask = ActivityTask.of(savedTask, requestUser, ActivityAction.INSERT);
        activityTaskRepository.save(activityTask);

        return TaskMapper.mapper(savedTask);
    }

    public TaskModel getTask(Long id) {
        return taskRepository.findById(id)
                .map(TaskMapper::mapper)
                .orElseThrow(taskNotFound);
    }

    public TaskModel getTaskForUser(Long id, String username) {
        Predicate<Task> isOwnTask = task -> task.getCreatedBy().equals(username);

        return Optional.of(taskRepository.findById(id))
                .orElseThrow(taskNotFound)
                .filter(isOwnTask)
                .map(TaskMapper::mapper)
                .orElseThrow(notOwnTask);
    }

    public List<TaskModel> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::mapper)
                .collect(Collectors.toList());
    }

    public List<TaskModel> getAllTaskByUser(Long userId) {
        List<Task> tasks = userservice.findById(userId)
                .map(taskRepository::findAllByAssignedUser)
                .orElseThrow(UserService.userNotFound);

        return tasks.stream()
                .map(TaskMapper::mapper)
                .collect(Collectors.toList());
    }

    public List<Task> findAllByProjectId(Long projectId) {
        final Project project = Optional.ofNullable(projectService.findProjectById(projectId))
                .orElseThrow(ProjectService.projectNotFound);
        return taskRepository.findAllByProject(project);
    }

    public List<TaskModel> getAllTaskByProject(Long projectId) {
        return findAllByProjectId(projectId)
                .stream()
                .map(TaskMapper::mapper)
                .collect(Collectors.toList());
    }

    public List<TaskModel> getAllExpiredTask() {
        return taskRepository.findAllByDueDateBefore(LocalDate.now())
                .stream()
                .map(TaskMapper::mapper)
                .collect(Collectors.toList());
    }

    public List<TaskModel> getAllTaskByStatus(TaskStatus status) {
        return taskRepository.findAllByStatus(status)
                .stream()
                .map(TaskMapper::mapper)
                .collect(Collectors.toList());
    }

    public void deleteTask(Long id, String requestUser) {
        Function<Task, ActivityTask> mapToActivity = task -> ActivityTask.of(task, requestUser, ActivityAction.DELETE);

        ActivityTask activityTask = taskRepository.findById(id)
                .map(mapToActivity)
                .orElseThrow(taskNotFound);

        taskRepository.deleteById(id);
        activityTaskRepository.save(activityTask);
    }

    public TaskModel updateTask(Long id, TaskEditRequest taskEditRequest, String requestUser) {
        Task task = taskRepository.getOne(id);
        if (task.getStatus() == TaskStatus.CLOSED) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "status", "error.task.status.closed.not.editable");
        }
        Project project = taskEditRequest.getProjectId() != null
                ? projectService.findProjectById(taskEditRequest.getProjectId())
                : task.getProject();

        User user = taskEditRequest.getAssignedUser() != null
                ? userservice.getOne(taskEditRequest.getAssignedUser())
                : project.getAssignedUser();
        TaskMapper.updateRequestToEntity(task, taskEditRequest, requestUser, project, user);

        Task savedTask = taskRepository.save(task);

        ActivityTask activityTask = ActivityTask.of(savedTask, requestUser, ActivityAction.UPDATE);
        activityTaskRepository.save(activityTask);

        return TaskMapper.mapper(savedTask);
    }
}
