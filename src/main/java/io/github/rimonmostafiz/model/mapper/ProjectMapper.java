package io.github.rimonmostafiz.model.mapper;

import io.github.rimonmostafiz.model.dto.ProjectModel;
import io.github.rimonmostafiz.model.entity.common.Status;
import io.github.rimonmostafiz.model.entity.db.Project;
import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.model.request.ProjectCreateRequest;

/**
 * @author Rimon Mostafiz
 */
public class ProjectMapper {
    public static ProjectModel mapper(Project entity) {
        ProjectModel model = new ProjectModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setStatus(entity.getStatus());
        if (entity.getAssignedUser() != null) {
            model.setAssignedUser(UserMapper.mapperForInternal(entity.getAssignedUser()));
        }
        return model;
    }

    public static ProjectModel mapperForInternal(Project entity) {
        ProjectModel model = new ProjectModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }

    public static Project modelToEntityMapperForCreate(ProjectCreateRequest createRequest, User createdBy) {
        Project entity = new Project();

        entity.setName(createRequest.getName());
        entity.setDescription(createRequest.getDescription());
        entity.setStatus(Status.getStatus(createRequest.getStatus()));
        entity.setAssignedUser(createdBy);

        entity.setCreatedBy(createdBy.getUsername());
        return entity;
    }
}
