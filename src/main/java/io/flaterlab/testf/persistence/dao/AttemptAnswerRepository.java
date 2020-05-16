package io.flaterlab.testf.persistence.dao;

import io.flaterlab.testf.persistence.model.Attempt;
import io.flaterlab.testf.persistence.model.AttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttemptAnswerRepository extends JpaRepository<AttemptAnswer, Long> {

    Optional<AttemptAnswer> findAttemptAnswerById(Long takeAnswerId);

    List<AttemptAnswer> findAllByAttempt(Attempt attempt);
}
