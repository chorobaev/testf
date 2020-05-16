package io.flaterlab.testf.persistence.dao;

import io.flaterlab.testf.persistence.model.Answer;
import io.flaterlab.testf.persistence.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findAnswerById(Long answerId);

    List<Answer> findAllByQuestion(Question question);
}
