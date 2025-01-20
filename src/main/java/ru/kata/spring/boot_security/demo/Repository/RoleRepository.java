package ru.kata.spring.boot_security.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Override
    List<Role> findAllById(Iterable<Long> longs);
}
