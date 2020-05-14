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

@RestController
@RequestMapping(value = "/v1/tests")
public class TestController {

    private ITestService testService;

    public TestController(ITestService testService) {
        this.testService = testService;
    }

    @GetMapping(value = "")
    public ResponseEntity allTests(@RequestParam(defaultValue = "20") int len) {
        return testService.getAllTests(len);
    }

    @GetMapping(value = "/{testId}")
    public ResponseEntity testById(@PathVariable("testId") long testId) {
        // TODO: restrict request only host
        return testService.getTestById(testId);
    }

    @PutMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity testEncoded(TestRequestDto body, Authentication auth) {
        FUserDetails userDetails = (FUserDetails) auth.getPrincipal();
        return testService.createNewTest(userDetails.getUser(), body);
    }

    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity testJson(@RequestBody TestRequestDto body, Authentication auth) {
        FUserDetails userDetails = (FUserDetails) auth.getPrincipal();
        return testService.createNewTest(userDetails.getUser(), body);
    }

    @PutMapping(value = "/{testId}/questions", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity questionEncoded(@PathVariable("testId") Long testId, QuestionRequestDto body) {
        return testService.addNewQuestion(testId, body);
    }

    @PutMapping(value = "/{testId}/questions", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity questionJson(@PathVariable("testId") Long testId, @RequestBody QuestionRequestDto body) {
        return testService.addNewQuestion(testId, body);
    }

    @PutMapping(value = "/questions/{questionId}/answers", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity answerEncoded(@PathVariable Long questionId, AnswerRequestDto body) {
        return testService.addNewAnswer(questionId, body);
    }

    @PutMapping(value = "/questions/{questionId}/answers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity answerJson(@PathVariable("questionId") Long questionId, @RequestBody AnswerRequestDto body) {
        return testService.addNewAnswer(questionId, body);
    }
}