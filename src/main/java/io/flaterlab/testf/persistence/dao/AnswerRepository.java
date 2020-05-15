package io.flaterlab.testf.persistence.dao;

import io.flaterlab.testf.persistence.model.Answer;
import io.flaterlab.testf.persistence.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findAllByQuestion(Question question);
}
