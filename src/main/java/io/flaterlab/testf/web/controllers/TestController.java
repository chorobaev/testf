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

@RestController
@RequestMapping(value = "/v1/tests")
public class TestController {

    private ITestService testService;

    public TestController(ITestService testService) {
        this.testService = testService;
    }

    @GetMapping(value = "")
    public ResponseEntity getAllTests(@RequestParam(defaultValue = "20") int len) {
        return testService.getAllTests(len);
    }

    @GetMapping(value = "/{testId}")
    public ResponseEntity getTestById(@PathVariable("testId") long testId) {
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
    public ResponseEntity addQuestionEncoded(@PathVariable("testId") Long testId, QuestionRequestDto body) {
        return testService.addNewQuestion(testId, body);
    }

    @PutMapping(value = "/{testId}/questions", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addQuestionJson(@PathVariable("testId") Long testId, @RequestBody QuestionRequestDto body) {
        return testService.addNewQuestion(testId, body);
    }

    @PutMapping(value = "/questions/{questionId}/answers", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity addAnswerEncoded(@PathVariable Long questionId, @Valid AnswerRequestDto body) {
        return testService.addNewAnswer(questionId, body);
    }

    @PutMapping(value = "/questions/{questionId}/answers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addAnswerJson(@PathVariable("questionId") Long questionId, @Valid @RequestBody AnswerRequestDto body) { return testService.addNewAnswer(questionId, body);
    }

    @PutMapping(value = "{testId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateTestEncoded(@PathVariable("testId") Long testId, TestRequestDto body) {
        return testService.updateTestById(testId, body);
    }

    @PutMapping(value = "{testId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTestJson(@PathVariable("testId") Long testId, @RequestBody TestRequestDto body) {
        return testService.updateTestById(testId, body);
    }

    @PutMapping(value = "/*/questions/{questionId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateQuestionEncoded(@PathVariable("questionId") Long testId, QuestionRequestDto body) {
        return testService.updateQuestionById(testId, body);
    }

    @PutMapping(value = "/*/questions/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateQuestionJson(@PathVariable("questionId") Long testId, @RequestBody QuestionRequestDto body) {
        return testService.updateQuestionById(testId, body);
    }

    @PutMapping(value = "/*/questions/answers/{answerId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateAnswerEncoded(@PathVariable("answerId") Long testId, AnswerRequestDto body) {
        return testService.updateAnswerById(testId, body);
    }

    @PutMapping(value = "/*/questions/answers/{answerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateAnswerJson(@PathVariable("answerId") Long testId, @RequestBody AnswerRequestDto body) {
        return testService.updateAnswerById(testId, body);
    }
}