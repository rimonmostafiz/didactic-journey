package io.github.rimonmostafiz.repository;

import io.github.rimonmostafiz.entity.db.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rimon Mostafiz
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
