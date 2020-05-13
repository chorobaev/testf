package io.flaterlab.testf.service;

import io.flaterlab.testf.web.dto.request.TestsRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

public interface ITestService {

    ResponseEntity getAllTests();

    ResponseEntity getAllTests(TestsRequestDto body);
}
