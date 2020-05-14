package io.flaterlab.testf.service;

import org.springframework.http.ResponseEntity;

public interface ITestService {

    ResponseEntity getAllTests(int size);

    ResponseEntity getTestById(long testId);
}
