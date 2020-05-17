package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.model.User;
import io.flaterlab.testf.web.dto.request.AnswerRequestDto;
import io.flaterlab.testf.web.dto.request.QuestionRequestDto;
import io.flaterlab.testf.web.dto.request.TestRequestDto;
import org.springframework.http.ResponseEntity;

public interface ITestService {

    ResponseEntity getAllTests(Integer size);

    ResponseEntity getTestById(Long testId);

    ResponseEntity createNewTest(User host, TestRequestDto body);

    ResponseEntity addNewQuestion(Long testId, QuestionRequestDto body);

    ResponseEntity addNewAnswer(Long questionId, AnswerRequestDto body);

    ResponseEntity updateTestById(Long testId, TestRequestDto body);

    ResponseEntity updateQuestionById(Long questionId, QuestionRequestDto body);

    ResponseEntity updateAnswerById(Long answerId, AnswerRequestDto body);

    ResponseEntity deleteTestById(Long testId);

    ResponseEntity deleteQuestionById(Long questionId);

    ResponseEntity deleteAnswerById(Long answerId);
}
