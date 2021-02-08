package io.github.rimonmostafiz.repository.activity;

import io.github.rimonmostafiz.entity.activity.ActivityProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rimon Mostafiz
 */
@Repository
public interface ActivityProjectRepository extends JpaRepository<ActivityProject, Long> {
}
