package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.dao.QuestionRepository;
import io.flaterlab.testf.persistence.dao.TestRepository;
import io.flaterlab.testf.persistence.model.Question;
import io.flaterlab.testf.persistence.model.Test;
import io.flaterlab.testf.web.dto.response.QuestionResponseDto;
import io.flaterlab.testf.web.dto.response.TestWithQuestionsResponseDto;
import io.flaterlab.testf.web.dto.response.TestResponseDto;
import io.flaterlab.testf.web.error.TestNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService implements ITestService {

    private TestRepository testRepository;
    private QuestionRepository questionRepository;

    public TestService(
        TestRepository testRepository,
        QuestionRepository questionRepository
    ) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public ResponseEntity getAllTests(int size) {
        return ResponseEntity.ok(retrieveTests(size));
    }

    private List<TestResponseDto> retrieveTests(int size) {
        Pageable published = PageRequest.of(0, size, Sort.by("createdAt"));
        List<Test> publishedTests = testRepository.findAllByPublishedTrue(published);

        return publishedTests.stream().map(this::testToDto).collect(Collectors.toList());
    }

    private TestResponseDto testToDto(Test test) {
        return TestResponseDto.builder()
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
            .build();
    }

    @Override
    public ResponseEntity getTestById(long testId) {
        return ResponseEntity.ok(retrieveTestAndQuestionsById(testId));
    }

    private TestWithQuestionsResponseDto retrieveTestAndQuestionsById(long id) {
        Test test = testRepository.findTestById(id).orElseThrow(() -> new TestNotFoundException(id));
        List<QuestionResponseDto> questionsDto = questionRepository.findAllByTest(test)
            .stream()
            .map(this::questionToDto)
            .collect(Collectors.toList());

        return TestWithQuestionsResponseDto.builder()
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
            .questions(questionsDto)
            .build();
    }

    private QuestionResponseDto questionToDto(Question question) {
        return QuestionResponseDto.builder()
            .id(question.getId())
            .testId(question.getTest().getId())
            .type(question.getType())
            .level(question.getLevel())
            .score(question.getScore())
            .content(question.getContent())
            .build();
    }
}
