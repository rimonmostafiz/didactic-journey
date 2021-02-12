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
import io.github.rimonmostafiz.model.request.TaskUpdateRequest;
import io.github.rimonmostafiz.repository.ProjectRepository;
import io.github.rimonmostafiz.repository.TaskRepository;
import io.github.rimonmostafiz.repository.UserRepository;
import io.github.rimonmostafiz.repository.activity.ActivityTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Rimon Mostafiz
 */
@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ActivityTaskRepository activityTaskRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private final Supplier<EntityNotFoundException> taskNotFound = () ->
            new EntityNotFoundException(HttpStatus.NO_CONTENT, "id", "No task found");

    Supplier<EntityNotFoundException> userNotFound = () ->
            new EntityNotFoundException(HttpStatus.NO_CONTENT, "id", "No user found");

    public TaskModel createTask(TaskCreateRequest taskCreateRequest, String requestUser) {
        Project project = projectRepository.getOne(taskCreateRequest.getProjectId());
        User user = userRepository.getOne(taskCreateRequest.getAssignedUser());
        Task task = TaskMapper.createRequestToEntity(taskCreateRequest, requestUser, project, user);

        entityManager.getReference(User.class, taskCreateRequest.getAssignedUser());
        Task savedTask = taskRepository.save(task);

        ActivityTask activityTask = new ActivityTask(savedTask, requestUser, ActivityAction.INSERT);
        activityTaskRepository.save(activityTask);

        return TaskMapper.mapper(savedTask);
    }

    public TaskModel getTask(Long id) {
        return taskRepository.findById(id)
                .map(TaskMapper::mapper)
                .orElseThrow(taskNotFound);
    }

    public List<TaskModel> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::mapper)
                .collect(Collectors.toList());
    }

    public List<TaskModel> getAllTaskByUser(Long userId) {
        List<Task> tasks = userRepository.findById(userId)
                .map(taskRepository::findAllByAssignedUser)
                .orElseThrow(userNotFound);

        return tasks.stream()
                .map(TaskMapper::mapper)
                .collect(Collectors.toList());
    }

    public List<TaskModel> getAllTaskByProject(Long projectId) {
        List<Task> tasks = projectRepository.findById(projectId)
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
        Function<Task, ActivityTask> mapToActivity = task ->
                new ActivityTask(task, requestUser, ActivityAction.DELETE);

        ActivityTask activityTask = taskRepository.findById(id)
                .map(mapToActivity)
                .orElseThrow(taskNotFound);

        taskRepository.deleteById(id);
        activityTaskRepository.save(activityTask);
    }

    public TaskModel updateTask(Long id, TaskUpdateRequest taskUpdateRequest, String requestUser) {
        Task task = taskRepository.getOne(id);
        if (task.getStatus() == TaskStatus.CLOSED) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "status", "CLOSED task is not editable");
        }
        Project project = projectRepository.getOne(taskUpdateRequest.getProjectId());
        User user = userRepository.getOne(taskUpdateRequest.getAssignedUser());
        TaskMapper.updateRequestToEntity(task, taskUpdateRequest, requestUser, project, user);

        Task savedTask = taskRepository.save(task);

        ActivityTask activityTask = new ActivityTask(savedTask, requestUser, ActivityAction.UPDATE);
        activityTaskRepository.save(activityTask);

        return TaskMapper.mapper(savedTask);
    }
}
