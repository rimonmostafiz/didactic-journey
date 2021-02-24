package io.github.rimonmostafiz.service.user;

import io.github.rimonmostafiz.DidacticJourneyApplication;
import io.github.rimonmostafiz.model.entity.common.Status;
import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.model.entity.db.UserRoles;
import io.github.rimonmostafiz.model.request.UserCreateRequest;
import io.github.rimonmostafiz.repository.UserRolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@SpringBootTest(classes = DidacticJourneyApplication.class)
@ActiveProfiles("dev")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Test
    void createUser() {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUsername("admin");
        userCreateRequest.setPassword("admin1");
        userCreateRequest.setEmail("admin@rimonmostafiz.com");
        userCreateRequest.setFirstName("Rimon");
        userCreateRequest.setLastName("Mostafiz");
        userCreateRequest.setStatus(Status.ACTIVE);

        User adminUser = userService.createUser(userCreateRequest, "System");
        log.debug("User: {}", adminUser);
        Assertions.assertNotNull(adminUser);

        UserRoles userRoles = new UserRoles();
        userRoles.setUserId(adminUser.getId());
        userRoles.setRoleId(1L);
        userRoles.setRoleName("ADMIN");
        userRoles.setCreateTime(LocalDateTime.now());
        userRoles.setCreatedBy("System");

        UserRoles savedUserRoles = userRolesRepository.save(userRoles);
        log.debug("UserRoles: {}", savedUserRoles);
        Assertions.assertNotNull(savedUserRoles);
    }
}