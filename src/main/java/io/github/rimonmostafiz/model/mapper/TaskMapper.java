package io.github.rimonmostafiz.model.mapper;

import io.github.rimonmostafiz.model.dto.TaskModel;
import io.github.rimonmostafiz.model.entity.common.TaskStatus;
import io.github.rimonmostafiz.model.entity.db.Project;
import io.github.rimonmostafiz.model.entity.db.Task;
import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.model.request.TaskCreateRequest;
import io.github.rimonmostafiz.model.request.TaskEditRequest;

/**
 * @author Rimon Mostafiz
 */
public class TaskMapper {

    public static TaskModel mapper(Task entity) {
        TaskModel model = new TaskModel();
        model.setId(entity.getId());
        model.setDescription(entity.getDescription());
        model.setStatus(entity.getStatus());
        model.setProject(ProjectMapper.mapperForInternal(entity.getProject()));
        model.setAssignedUser(UserMapper.mapperForInternal(entity.getAssignedUser()));
        model.setDueDate(entity.getDueDate());
        return model;
    }

    public static Task createRequestToEntity(TaskCreateRequest taskCreateRequest, String createdBy, Project project, User user) {
        Task entity = new Task();

        entity.setDescription(taskCreateRequest.getDescription());
        entity.setStatus(TaskStatus.getStatus(taskCreateRequest.getStatus()));
        entity.setProject(project);
        entity.setAssignedUser(user);
        entity.setDueDate(taskCreateRequest.getDueDate());

        entity.setCreatedBy(createdBy);
        return entity;
    }

    public static void updateRequestToEntity(Task entity, TaskEditRequest taskEditRequest, String createdBy, Project project, User user) {

        entity.setDescription(taskEditRequest.getDescription());
        entity.setStatus(TaskStatus.getStatus(taskEditRequest.getStatus()));
        entity.setProject(project);
        entity.setAssignedUser(user);
        entity.setDueDate(taskEditRequest.getDueDate());

        entity.setCreatedBy(createdBy);
    }
}
