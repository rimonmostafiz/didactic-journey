package io.github.rimonmostafiz.repository.activity;

import io.github.rimonmostafiz.model.entity.activity.ActivityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rimon Mostafiz
 */
@Repository
public interface ActivityUserRepository extends JpaRepository<ActivityUser, Long> {
}
