package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.Repository.RoleRepository;
import ru.kata.spring.boot_security.demo.Repository.UserRepository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    UserRepository userRepository;

    RoleRepository roleRepository;

    @Autowired
    public UserService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден ;(");
        } else {
            return user;
        }
    }
    @Transactional
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(new User());
    }
    @Transactional
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveUser(User user) {
        User userFrDb = userRepository.findByUsername(user.getUsername());

        if (userFrDb != null) {
            return;
        }
        user.getAuthorities();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    @Transactional
    public void saveAdmin(User user) {
        User admin = userRepository.findByUsername(user.getUsername());
        if (admin != null) {
            return;
        }
        user.getAuthorities();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        User updating = userRepository.findByUsername(user.getUsername());
        if (updating != null) {
            updating.setUsername(user.getUsername());
            updating.setAge(user.getAge());
            updating.setPassword(user.getPassword());
            updating.setEmail(user.getEmail());
            userRepository.save(updating);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
