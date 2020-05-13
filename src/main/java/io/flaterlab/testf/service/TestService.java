package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.dao.TestRepository;
import io.flaterlab.testf.persistence.model.Test;
import io.flaterlab.testf.web.dto.request.TestsRequestDto;
import io.flaterlab.testf.web.dto.response.TestsResponseDto;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService implements ITestService {

    private TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public ResponseEntity getAllTests(int size) {
        return ResponseEntity.ok(retrieveTests(size));
    }

    private List<TestsResponseDto> retrieveTests(int size) {
        System.out.println("Len of tests: " + size);

        Pageable published = PageRequest.of(0, size, Sort.by("createdAt"));
        List<Test> publishedTests = testRepository.findAllByPublishedTrue(published);

        return publishedTests.stream().map(test ->
            TestsResponseDto.builder()
                .id(test.getId())
                .hostId(test.getUser().getId())
                .title(test.getTitle())
                .slug(test.getSlug())
                .summary(test.getSummary())
                .type(test.getType())
                .score(test.getScore())
                .startsAt(test.getStartsAt())
                .endsAt(test.getEndsAt())
                .content(test.getContent())
                .build()
        ).collect(Collectors.toList());
    }
}
