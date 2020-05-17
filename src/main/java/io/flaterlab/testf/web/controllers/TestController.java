package io.flaterlab.testf.web.controllers;

import io.flaterlab.testf.security.FUserDetails;
import io.flaterlab.testf.service.ITestService;
import io.flaterlab.testf.web.dto.request.AnswerRequestDto;
import io.flaterlab.testf.web.dto.request.QuestionRequestDto;
import io.flaterlab.testf.web.dto.request.TestRequestDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.OptionalLong;

@RestController
@RequestMapping(value = "/v1/tests")
public class TestController {

    private ITestService testService;

    public TestController(ITestService testService) {
        this.testService = testService;
    }

    @GetMapping(value = "")
    public ResponseEntity getAllTests(@RequestParam(defaultValue = "20") int size,
                                      @RequestParam(required = false) Long userId) {
        return userId != null ? testService.getAllTestsOfUser(userId, size) : testService.getAllTests(size);
    }

    @GetMapping(value = "/{testId}")
    public ResponseEntity getTestById(@PathVariable Long testId) {
        // TODO: restrict request only host
        return testService.getTestById(testId);
    }

    @PutMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity createTestEncoded(TestRequestDto body, Authentication auth) {
        FUserDetails userDetails = (FUserDetails) auth.getPrincipal();
        return testService.createNewTest(userDetails.getUser(), body);
    }

    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTestJson(@RequestBody TestRequestDto body, Authentication auth) {
        FUserDetails userDetails = (FUserDetails) auth.getPrincipal();
        return testService.createNewTest(userDetails.getUser(), body);
    }

    @PutMapping(value = "/{testId}/questions", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity addQuestionEncoded(@PathVariable Long testId, QuestionRequestDto body) {
        return testService.addNewQuestion(testId, body);
    }

    @PutMapping(value = "/{testId}/questions", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addQuestionJson(@PathVariable Long testId, @RequestBody QuestionRequestDto body) {
        return testService.addNewQuestion(testId, body);
    }

    @PutMapping(value = "/questions/{questionId}/answers", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity addAnswerEncoded(@PathVariable Long questionId, @Valid AnswerRequestDto body) {
        return testService.addNewAnswer(questionId, body);
    }

    @PutMapping(value = "/questions/{questionId}/answers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addAnswerJson(@PathVariable Long questionId, @Valid @RequestBody AnswerRequestDto body) {
        return testService.addNewAnswer(questionId, body);
    }

    @PutMapping(value = "{testId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateTestEncoded(@PathVariable Long testId, TestRequestDto body) {
        return testService.updateTestById(testId, body);
    }

    @PutMapping(value = "{testId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTestJson(@PathVariable Long testId, @RequestBody TestRequestDto body) {
        return testService.updateTestById(testId, body);
    }

    @PutMapping(value = "/questions/{questionId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateQuestionEncoded(@PathVariable Long questionId, QuestionRequestDto body) {
        return testService.updateQuestionById(questionId, body);
    }

    @PutMapping(value = "/questions/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateQuestionJson(@PathVariable Long questionId, @RequestBody QuestionRequestDto body) {
        return testService.updateQuestionById(questionId, body);
    }

    @PutMapping(value = "/questions/answers/{answerId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateAnswerEncoded(@PathVariable Long answerId, AnswerRequestDto body) {
        return testService.updateAnswerById(answerId, body);
    }

    @PutMapping(value = "/questions/answers/{answerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateAnswerJson(@PathVariable Long answerId, @RequestBody AnswerRequestDto body) {
        return testService.updateAnswerById(answerId, body);
    }

    @DeleteMapping(value = "/{testId}")
    public ResponseEntity deleteTest(@PathVariable Long testId) {
        return testService.deleteTestById(testId);
    }

    @DeleteMapping(value = "/questions/{questionId}")
    public ResponseEntity deleteQuestion(@PathVariable Long questionId) {
        return testService.deleteQuestionById(questionId);
    }

    @DeleteMapping(value = "/questions/answers/{answerId}")
    public ResponseEntity deleteAnswer(@PathVariable Long answerId) {
        return testService.deleteAnswerById(answerId);
    }
}