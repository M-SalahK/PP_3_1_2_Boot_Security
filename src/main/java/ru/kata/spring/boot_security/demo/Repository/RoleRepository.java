package ru.kata.spring.boot_security.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

}
