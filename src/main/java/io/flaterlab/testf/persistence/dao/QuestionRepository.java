package io.flaterlab.testf.persistence.dao;

import io.flaterlab.testf.persistence.model.Question;
import io.flaterlab.testf.persistence.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findQuestionById(Long questionId);

    Optional<Question> findQuestionByContent(String content);

    List<Question> findAllByTest(Test test);
}
