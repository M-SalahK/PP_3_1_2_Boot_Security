package ru.kata.spring.boot_security.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.Repository.RoleRepository;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> findRoleById(List<Long> id) {
        return roleRepository.findAllById(id);
    }

    public List<Role> defaultRole() {
        Role defaultRole = roleRepository.findById(2L).orElseThrow(() -> new RuntimeException("Default role not found"));
        return Collections.singletonList(defaultRole);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
