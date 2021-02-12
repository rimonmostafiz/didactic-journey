package io.github.rimonmostafiz.model.mapper;

import io.github.rimonmostafiz.model.dto.UserModel;
import io.github.rimonmostafiz.model.entity.db.User;

/**
 * @author Rimon Mostafiz
 */
public class UserMapper {

    public static UserModel mapper(User entity) {
        UserModel model = new UserModel();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setEmail(entity.getEmail());
        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        model.setStatus(entity.getStatus());
        return model;
    }
}
