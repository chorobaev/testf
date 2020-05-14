package io.flaterlab.testf.persistence.dao;

import io.flaterlab.testf.persistence.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
