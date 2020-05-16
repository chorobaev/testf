package io.flaterlab.testf.web.controllers;

import io.flaterlab.testf.security.FUserDetails;
import io.flaterlab.testf.service.IAttemptService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/attempt")
public class AttemptController {

    private IAttemptService attemptService;

    public AttemptController(IAttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @GetMapping(value = "/{testId}")
    public ResponseEntity attemptTest(@PathVariable Long testId, Authentication auth) {
        FUserDetails userDetails = (FUserDetails) auth.getPrincipal();
        return attemptService.attemptTestById(userDetails.getUser(), testId);
    }

    @PostMapping(value = "/{attemptId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity finishAttempt(@PathVariable Long attemptId, @RequestBody List<Long> answerIds) {
        return attemptService.finishAttemptById(attemptId, answerIds);
    }
}
