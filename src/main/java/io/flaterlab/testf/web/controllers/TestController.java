package io.flaterlab.testf.web.controllers;

import io.flaterlab.testf.service.ITestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/v1/tests")
public class TestController {

    private ITestService testService;

    public TestController(ITestService testService) {
        this.testService = testService;
    }

    @GetMapping(value = "")
    public ResponseEntity all(@RequestParam(defaultValue = "20") int len) {
        return testService.getAllTests(len);
    }

    @GetMapping(value = "/{testId}")
    public ResponseEntity testById(@PathVariable("testId") long testId) {
        // TODO: restrict request only host
        return testService.getTestById(testId);
    }

}