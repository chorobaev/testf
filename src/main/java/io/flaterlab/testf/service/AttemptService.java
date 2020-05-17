package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.dao.*;
import io.flaterlab.testf.persistence.model.*;
import io.flaterlab.testf.web.dto.response.AttemptAnswerResponseDto;
import io.flaterlab.testf.web.dto.response.AttemptQuestionResponseDto;
import io.flaterlab.testf.web.dto.response.AttemptResponseDto;
import io.flaterlab.testf.web.dto.response.AttemptResultResponseDto;
import io.flaterlab.testf.web.error.AnswerNotFoundException;
import io.flaterlab.testf.web.error.BadRequestException;
import io.flaterlab.testf.web.error.TestNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttemptService implements IAttemptService {

    private TestRepository testRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private AttemptRepository attemptRepository;
    private AttemptAnswerRepository attemptAnswerRepository;

    public AttemptService(
        TestRepository testRepository,
        QuestionRepository questionRepository,
        AnswerRepository answerRepository,
        AttemptRepository attemptRepository,
        AttemptAnswerRepository attemptAnswerRepository
    ) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.attemptRepository = attemptRepository;
        this.attemptAnswerRepository = attemptAnswerRepository;
    }

    @Override
    public ResponseEntity attemptTestById(User user, Long testId) {
        Test test = testRepository.findTestById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        Attempt attempt = saveAttempt(user, test);

        AttemptResponseDto responseDto = new AttemptResponseDto();
        BeanUtils.copyProperties(test, responseDto);
        responseDto.setAttemptId(attempt.getId());
        responseDto.setHostId(test.getUser().getId());

        List<AttemptQuestionResponseDto> questionsDto = questionRepository.findAllByTest(test)
            .stream()
            .map(this::questionToDto)
            .collect(Collectors.toList());
        responseDto.setQuestions(questionsDto);

        return ResponseEntity.ok(responseDto);
    }

    private AttemptQuestionResponseDto questionToDto(Question question) {
        AttemptQuestionResponseDto responseDto = new AttemptQuestionResponseDto();
        BeanUtils.copyProperties(question, responseDto);

        List<AttemptAnswerResponseDto> answersDto = answerRepository.findAllByQuestion(question)
            .stream()
            .map(this::answerToDto)
            .collect(Collectors.toList());
        responseDto.setAnswers(answersDto);

        return responseDto;
    }

    private AttemptAnswerResponseDto answerToDto(Answer answer) {
        AttemptAnswerResponseDto responseDto = new AttemptAnswerResponseDto();
        BeanUtils.copyProperties(answer, responseDto);

        return responseDto;
    }

    private Attempt saveAttempt(User user, Test test) {
        Date now = new Date();
        Attempt attempt = Attempt.builder()
            .user(user)
            .test(test)
            .status(Attempt.Status.STARTED)
            .totalScore(0)
            .published(true)
            .createdAt(now)
            .startedAt(now)
            .build();

        return attemptRepository.save(attempt);
    }

    @Override
    public ResponseEntity finishAttemptById(Long attemptId, List<Long> answerIds) {
        Attempt attempt = attemptRepository.findAttemptById(attemptId).orElseThrow(() ->
            new BadRequestException("Attempt with id '" + attemptId + "' doesn't exists"));

        AttemptResultResponseDto response = prepareAttemptResult(attempt, answerIds);

        attempt.setStatus(Attempt.Status.FINISHED);
        attempt.setFinishedAt(new Date());
        attempt.setTotalQuestions(response.getTotalQuestions());
        attempt.setCorrectAnswers(response.getCorrectAnswers());
        attempt.setTotalScore(response.getTotalScore());
        attempt.setEarnedScore(response.getEarnedScore());
        attemptRepository.save(attempt);

        return ResponseEntity.ok(response);
    }

    private AttemptResultResponseDto prepareAttemptResult(Attempt attempt, List<Long> answerIds) {
        List<Question> questions = questionRepository.findAllByTest(attempt.getTest());

        int totalScore = questions.stream()
            .mapToInt(Question::getScore)
            .sum();

        List<Answer> correctAnswers = answerIds.stream()
            .map(id -> answerRepository.findAnswerById(id).orElseThrow(() -> new AnswerNotFoundException(id)))
            .filter(answer -> answer.getCorrect().equals(true))
            .collect(Collectors.toList());

        correctAnswers.forEach(answer -> saveAttempt(answer, attempt));

        int earnedScore = correctAnswers.stream()
            .mapToInt(answer -> answer.getQuestion().getScore())
            .sum();

        return AttemptResultResponseDto.builder()
            .attemptId(attempt.getId())
            .testId(attempt.getTest().getId())
            .testTitle(attempt.getTest().getTitle())
            .totalQuestions(questions.size())
            .correctAnswers(correctAnswers.size())
            .totalScore(totalScore)
            .earnedScore(earnedScore)
            .content("")
            .build();
    }

    private void saveAttempt(Answer answer, Attempt attempt) {
        attemptAnswerRepository.save(
            AttemptAnswer.builder()
                .attempt(attempt)
                .answer(answer)
                .createdAt(new Date())
                .active(true)
                .build()
        );
    }

    @Override
    public ResponseEntity getAttemptHistory(User user, Integer size) {
        Pageable latest = PageRequest.of(0, size, Sort.by("finishedAt"));
        List<AttemptResultResponseDto> responseDto = attemptRepository.findAllByUser(user, latest).stream()
            .map(this::attemptToDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(responseDto);
    }

    private AttemptResultResponseDto attemptToDto(Attempt attempt) {
        AttemptResultResponseDto responseDto = new AttemptResultResponseDto();
        BeanUtils.copyProperties(attempt, responseDto);
        responseDto.setAttemptId(attempt.getId());
        responseDto.setTestId(attempt.getTest().getId());
        responseDto.setTestTitle(attempt.getTest().getTitle());

        return responseDto;
    }
}
