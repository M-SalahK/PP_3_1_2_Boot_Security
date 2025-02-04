package ru.kata.spring.boot_security.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.Repository.UserRepository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(new User());
    }

    public User getPrincipalUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveUser(User user, List<Role> roles) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("User with this login was not found!");
        }
        if (user.getRoles().isEmpty()) {
            user.setRoles(roleService.defaultRole());
        } else {
            user.setRoles(roles);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user, List<Long> roles, Long id) {
        User userForUpdate = findUserById(id);
        userForUpdate.setUsername(user.getUsername());
        userForUpdate.setAge(user.getAge());
        userForUpdate.setEmail(user.getEmail());
        userForUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userForUpdate.getRoles().clear();
        userForUpdate.setRoles(roleService.findRoleById(roles));
        userRepository.save(userForUpdate);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
