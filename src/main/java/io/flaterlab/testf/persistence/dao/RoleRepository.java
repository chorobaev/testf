package io.flaterlab.testf.persistence.dao;

import io.flaterlab.testf.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @Override
    void delete(Role role);

}
