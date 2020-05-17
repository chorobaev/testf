package io.flaterlab.testf.persistence.dao;

import io.flaterlab.testf.persistence.model.Attempt;
import io.flaterlab.testf.persistence.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    Optional<Attempt> findAttemptById(Long takeId);

    List<Attempt> findAllByUser(User user);

    List<Attempt> findAllByUser(User user, Pageable pageable);
}
