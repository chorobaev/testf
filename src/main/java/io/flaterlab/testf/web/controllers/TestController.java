package io.flaterlab.testf.web.controllers;

import io.flaterlab.testf.service.ITestService;
import io.flaterlab.testf.web.dto.request.TestsRequestDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tests")
public class TestController {

    private ITestService testService;

    public TestController(ITestService testService) {
        this.testService = testService;
    }

    @GetMapping(value = "")
    public ResponseEntity all() {
        return testService.getAllTests();
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity allEncoded(TestsRequestDto body) {
        return testService.getAllTests(body);
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity allJson(@RequestBody TestsRequestDto body) {
        return testService.getAllTests(body);
    }

}
