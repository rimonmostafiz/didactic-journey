package io.github.rimonmostafiz.repository;

import io.github.rimonmostafiz.model.entity.db.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rimon Mostafiz
 */
@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
}
