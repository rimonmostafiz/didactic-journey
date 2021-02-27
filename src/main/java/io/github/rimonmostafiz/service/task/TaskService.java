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

    private final Supplier<EntityNotFoundException> taskNotFound = () ->
            new EntityNotFoundException(HttpStatus.BAD_REQUEST, "taskId", "error.task.not.found");

    private final Supplier<EntityNotFoundException> userNotFound = () ->
            new EntityNotFoundException(HttpStatus.BAD_REQUEST, "userId", "error.user.not.found");

    private final Supplier<ValidationException> notOwnTask = () ->
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

    public TaskModel getTaskUser(Long id, String username) {
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
                .orElseThrow(userNotFound);

        return tasks.stream()
                .map(TaskMapper::mapper)
                .collect(Collectors.toList());
    }

    public List<TaskModel> getAllTaskByProject(Long projectId) {
        List<Task> tasks = Optional.ofNullable(projectService.findProjectById(projectId))
                .map(taskRepository::findAllByProject)
                .orElseThrow(userNotFound);

        return tasks.stream()
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
        Project project = projectService.findProjectById(taskEditRequest.getProjectId());
        User user = userservice.getOne(taskEditRequest.getAssignedUser());
        TaskMapper.updateRequestToEntity(task, taskEditRequest, requestUser, project, user);

        Task savedTask = taskRepository.save(task);

        ActivityTask activityTask = ActivityTask.of(savedTask, requestUser, ActivityAction.UPDATE);
        activityTaskRepository.save(activityTask);

        return TaskMapper.mapper(savedTask);
    }
}
