package io.flaterlab.testf.web.controllers;

import io.flaterlab.testf.service.ITestService;
import io.flaterlab.testf.web.dto.request.TestsRequestDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tests")
public class TestController {

    private ITestService testService;

    public TestController(ITestService testService) {
        this.testService = testService;
    }

    @GetMapping(value = "")
    public ResponseEntity all(@RequestParam(defaultValue = "20") int len) {
        return testService.getAllTests(len);
    }

}
