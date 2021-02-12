package io.github.rimonmostafiz.service.user;

import io.github.rimonmostafiz.component.exception.UserNotFoundException;
import io.github.rimonmostafiz.model.entity.common.Status;
import io.github.rimonmostafiz.model.entity.db.Role;
import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    Predicate<User> isNotInactive = user -> !user.getStatus().equals(Status.INACTIVE);
    Function<User, Pair<Status, List<Role>>> mapToStatusAndRoleList = user -> Pair.of(user.getStatus(), user.getRoles());

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
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
