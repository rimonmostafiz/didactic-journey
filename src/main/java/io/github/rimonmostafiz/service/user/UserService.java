package io.github.rimonmostafiz.service.user;

import io.github.rimonmostafiz.component.exception.UserNotFoundException;
import io.github.rimonmostafiz.model.entity.activity.ActivityUser;
import io.github.rimonmostafiz.model.entity.common.ActivityAction;
import io.github.rimonmostafiz.model.entity.common.Status;
import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.model.entity.db.UserRoles;
import io.github.rimonmostafiz.model.mapper.UserMapper;
import io.github.rimonmostafiz.model.request.UserCreateRequest;
import io.github.rimonmostafiz.repository.UserRepository;
import io.github.rimonmostafiz.repository.activity.ActivityUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ActivityUserRepository activityUserRepository;

    Predicate<User> isNotInactive = user -> !user.getStatus().equals(Status.INACTIVE);

    public User createUser(UserCreateRequest userCreateRequest, String requestUser) {
        User user = UserMapper.mapUserCreateRequest(userCreateRequest, requestUser, passwordEncoder);
        User savedUser = userRepository.saveAndFlush(user);

        ActivityUser activityUser = ActivityUser.of(savedUser, requestUser, ActivityAction.INSERT);
        log.debug("Activity User: {}", activityUser.toString());
        activityUserRepository.saveAndFlush(activityUser);

        return savedUser;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User Not found!"));
    }

    public void ifDisableThrowException(User user) {
        Optional.ofNullable(user)
                .filter(isNotInactive)
                .orElseThrow(() -> new DisabledException("User is INACTIVE"));
    }

    public List<String> getUserRoles(User user) {
        return user.getRoles()
                .stream()
                .map(UserRoles::getRoleName)
                .collect(Collectors.toList());
    }
}
