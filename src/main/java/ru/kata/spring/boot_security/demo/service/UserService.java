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
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    EntityManager entityManager;

    UserRepository userRepository;
    @Autowired
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
        Optional<User> user = userRepository.findById((id));
        return user.orElse(new User());
    }
    @Transactional
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) throws Exception {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            throw new Exception("Harshara");
        }

        user.setRoles(roleRepository.findAll());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long id, User user) {
        User findUser = entityManager.find(User.class, id);
        findUser.setUsername(user.getUsername());
        findUser.setAge(user.getAge());
        findUser.setEmail(user.getEmail());
        findUser.setPassword(user.getPassword());
        userRepository.save(findUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
