package io.github.rimonmostafiz.utils.mapper;

import io.github.rimonmostafiz.entity.db.Project;
import io.github.rimonmostafiz.model.ProjectModel;

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
        model.setAssignedUser(UserMapper.mapper(entity.getAssignedUser()));
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
