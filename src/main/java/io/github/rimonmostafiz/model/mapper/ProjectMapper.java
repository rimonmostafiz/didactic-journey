package io.github.rimonmostafiz.model.mapper;

import io.github.rimonmostafiz.model.dto.ProjectModel;
import io.github.rimonmostafiz.model.entity.db.Project;

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
        model.setAssignedUser(UserMapper.mapUserCreateRequest(entity.getAssignedUser()));
        return model;
    }

    public static Project modelToEntityMapperForCreate(ProjectModel model, String createdBy) {
        Project entity = new Project();

        entity.setDescription(model.getDescription());
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setStatus(model.getStatus());

        entity.setCreatedBy(createdBy);
        return entity;
    }
}
