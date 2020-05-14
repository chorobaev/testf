package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.dao.AnswerRepository;
import io.flaterlab.testf.persistence.dao.QuestionRepository;
import io.flaterlab.testf.persistence.dao.TestRepository;
import io.flaterlab.testf.persistence.model.Answer;
import io.flaterlab.testf.persistence.model.Question;
import io.flaterlab.testf.persistence.model.Test;
import io.flaterlab.testf.persistence.model.User;
import io.flaterlab.testf.utils.Json;
import io.flaterlab.testf.web.dto.request.AnswerRequestDto;
import io.flaterlab.testf.web.dto.request.QuestionRequestDto;
import io.flaterlab.testf.web.dto.request.TestRequestDto;
import io.flaterlab.testf.web.dto.response.QuestionResponseDto;
import io.flaterlab.testf.web.dto.response.TestWithQuestionsResponseDto;
import io.flaterlab.testf.web.dto.response.TestResponseDto;
import io.flaterlab.testf.web.error.BadRequestException;
import io.flaterlab.testf.web.error.TestNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService implements ITestService {

    private TestRepository testRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    public TestService(
        TestRepository testRepository,
        QuestionRepository questionRepository,
        AnswerRepository answerRepository
    ) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
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

    @Override
    public ResponseEntity createNewTest(User host, TestRequestDto body) {
        if (testRepository.findTestByTitleAndUser(body.getTitle(), host).isPresent()) {
            throw new BadRequestException("Test with title '" + body.getTitle() + "' already exists");
        }

        Test test = new Test();
        BeanUtils.copyProperties(body, test);

        test.setUser(host);
        test.setScore(0);
        test.setPublished(body.getPublished() == null ? true : body.getPublished());

        Calendar c = Calendar.getInstance();
        test.setCreatedAt(c.getTime());
        if (body.getStartsAt() == null) {
            test.setStartsAt(c.getTime());
        }
        if (body.getEndsAt() == null) {
            c.add(Calendar.YEAR, 10);
            test.setEndsAt(c.getTime());
        }

        testRepository.save(test);

        return ResponseEntity.ok(Json.builder().put("message", "Success").buildMap());
    }

    @Override
    public ResponseEntity addNewQuestion(Long testId, QuestionRequestDto body) {
        Test test = testRepository.findTestById(testId).orElseThrow(() ->
            new BadRequestException("Test with id '" + testId + "' doesn't exist"));

        if (questionRepository.findQuestionByContent(body.getContent()).isPresent()) {
            throw new BadRequestException("Question with content '" + body.getContent() + "' already exists");
        }

        Question question = new Question();
        BeanUtils.copyProperties(body, question);
        question.setTest(test);
        question.setActive(body.getActive() == null ? false : body.getActive());
        question.setCreatedAt(new Date());

        questionRepository.save(question);

        return ResponseEntity.ok(Json.builder().put("message", "Success").buildMap());
    }

    @Override
    public ResponseEntity addNewAnswer(Long questionId, AnswerRequestDto body) {
        Question question = questionRepository.findById(questionId).orElseThrow(() ->
            new BadRequestException("Question with id '" + questionId + "' doesn't exist"));

        Answer answer = new Answer();
        BeanUtils.copyProperties(body, answer);
        answer.setQuestion(question);
        answer.setActive(body.getActive() == null ? true : body.getActive());
        answer.setCreatedAt(new Date());

        answerRepository.save(answer);

        return ResponseEntity.ok(Json.builder().put("message", "Success").buildMap());
    }
}
