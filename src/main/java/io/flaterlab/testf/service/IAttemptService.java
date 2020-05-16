package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAttemptService {

    ResponseEntity attemptTestById(User user, Long testId);

    ResponseEntity finishAttemptById(Long takeId, List<Long> answerIds);

}
