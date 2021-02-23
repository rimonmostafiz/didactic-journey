package io.github.rimonmostafiz.service.user;

import io.github.rimonmostafiz.model.entity.db.User;
import io.github.rimonmostafiz.model.entity.db.UserRoles;
import io.github.rimonmostafiz.repository.UserRepository;
import io.github.rimonmostafiz.service.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rimon Mostafiz
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserPrincipal(user.getUsername(), user.getPassword(), getUserRoles(user));
    }

    public List<String> getUserRoles(User user) {
        return user.getRoles()
                .stream()
                .map(UserRoles::getRoleName)
                .collect(Collectors.toList());
    }
}
