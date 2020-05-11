package io.flaterlab.testf.persistence.dao;

import io.flaterlab.testf.persistence.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Optional<Privilege> findByName(String name);

    @Override
    void delete(Privilege privilege);

}
