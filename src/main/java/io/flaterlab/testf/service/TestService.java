package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.dao.AnswerRepository;
import io.flaterlab.testf.persistence.dao.QuestionRepository;
import io.flaterlab.testf.persistence.dao.TestRepository;
import io.flaterlab.testf.persistence.model.Answer;
import io.flaterlab.testf.persistence.model.Question;
import io.flaterlab.testf.persistence.model.Test;
import io.flaterlab.testf.persistence.model.User;
import io.flaterlab.testf.utils.Json;
import io.flaterlab.testf.utils.Transfer;
import io.flaterlab.testf.web.dto.request.AnswerRequestDto;
import io.flaterlab.testf.web.dto.request.QuestionRequestDto;
import io.flaterlab.testf.web.dto.request.TestRequestDto;
import io.flaterlab.testf.web.dto.response.AnswerResponseDto;
import io.flaterlab.testf.web.dto.response.QuestionResponseDto;
import io.flaterlab.testf.web.dto.response.TestResponseDto;
import io.flaterlab.testf.web.dto.response.TestWithQuestionsResponseDto;
import io.flaterlab.testf.web.error.AnswerNotFoundException;
import io.flaterlab.testf.web.error.BadRequestException;
import io.flaterlab.testf.web.error.QuestionNotFoundException;
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
    public ResponseEntity getAllTests(Integer size) {
        Pageable published = PageRequest.of(0, size, Sort.by("createdAt"));
        List<Test> publishedTests = testRepository.findAllByPublishedTrue(published);
        List<TestResponseDto> responseDto = publishedTests.stream().map(this::testToDto).collect(Collectors.toList());

        return ResponseEntity.ok(responseDto);
    }

    private TestResponseDto testToDto(Test test) {
        TestResponseDto responseDto = new TestResponseDto();
        BeanUtils.copyProperties(test, responseDto);

        return responseDto;
    }

    @Override
    public ResponseEntity getTestById(Long testId) {
        Test test = testRepository.findTestById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        TestWithQuestionsResponseDto responseDto = new TestWithQuestionsResponseDto();
        BeanUtils.copyProperties(test, responseDto);
        responseDto.setHostId(test.getUser().getId());

        List<QuestionResponseDto> questionsDto = questionRepository.findAllByTest(test)
            .stream()
            .map(this::questionToDto)
            .collect(Collectors.toList());
        responseDto.setQuestions(questionsDto);

        return ResponseEntity.ok(responseDto);
    }

    private QuestionResponseDto questionToDto(Question question) {
        QuestionResponseDto responseDto = new QuestionResponseDto();
        BeanUtils.copyProperties(question, responseDto);
        responseDto.setTestId(question.getTest().getId());

        List<AnswerResponseDto> answersDto = answerRepository.findAllByQuestion(question)
            .stream()
            .map(this::answerToDto)
            .collect(Collectors.toList());
        responseDto.setAnswers(answersDto);

        return responseDto;
    }

    private AnswerResponseDto answerToDto(Answer answer) {
        AnswerResponseDto responseDto = new AnswerResponseDto();
        BeanUtils.copyProperties(answer, responseDto);

        return responseDto;
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

        return ResponseEntity.ok(Json.messageSuccess());
    }

    @Override
    public ResponseEntity addNewQuestion(Long testId, QuestionRequestDto body) {
        Test test = testRepository.findTestById(testId).orElseThrow(() ->
            new TestNotFoundException(testId));

        if (questionRepository.findQuestionByContent(body.getContent()).isPresent()) {
            throw new BadRequestException("Question with content '" + body.getContent() + "' already exists");
        }

        Question question = new Question();
        BeanUtils.copyProperties(body, question);
        question.setTest(test);
        question.setActive(body.getActive() == null ? false : body.getActive());
        question.setCreatedAt(new Date());

        questionRepository.save(question);

        return ResponseEntity.ok(Json.messageSuccess());
    }

    @Override
    public ResponseEntity addNewAnswer(Long questionId, AnswerRequestDto body) {
        Question question = questionRepository.findById(questionId).orElseThrow(() ->
            new QuestionNotFoundException(questionId));

        Answer answer = new Answer();
        BeanUtils.copyProperties(body, answer);
        answer.setQuestion(question);
        answer.setActive(body.getActive() == null ? true : body.getActive());
        answer.setCreatedAt(new Date());

        answerRepository.save(answer);

        return ResponseEntity.ok(Json.messageSuccess());
    }

    @Override
    public ResponseEntity updateTestById(Long testId, TestRequestDto body) {
        Test test = testRepository.findTestById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        Transfer.notNullProperties(body, test);
        test.setUpdatedAt(new Date());
        testRepository.save(test);

        return ResponseEntity.ok(Json.messageSuccess());
    }

    @Override
    public ResponseEntity updateQuestionById(Long questionId, QuestionRequestDto body) {
        Question question = questionRepository.findQuestionById(questionId).orElseThrow(() ->
            new QuestionNotFoundException(questionId));
        question.setUpdatedAt(new Date());
        Transfer.notNullProperties(body, question);
        questionRepository.save(question);

        return ResponseEntity.ok(Json.messageSuccess());
    }

    @Override
    public ResponseEntity updateAnswerById(Long answerId, AnswerRequestDto body) {
        Answer answer = answerRepository.findAnswerById(answerId).orElseThrow(() ->
            new AnswerNotFoundException(answerId));
        answer.setUpdatedAt(new Date());
        Transfer.notNullProperties(body, answer);
        answerRepository.save(answer);

        return ResponseEntity.ok(Json.messageSuccess());
    }
}
