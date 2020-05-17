package io.flaterlab.testf.security;

import io.flaterlab.testf.persistence.dao.AnswerRepository;
import io.flaterlab.testf.persistence.dao.AttemptRepository;
import io.flaterlab.testf.persistence.dao.QuestionRepository;
import io.flaterlab.testf.persistence.dao.TestRepository;
import io.flaterlab.testf.persistence.model.Answer;
import io.flaterlab.testf.persistence.model.Attempt;
import io.flaterlab.testf.persistence.model.Question;
import io.flaterlab.testf.persistence.model.Test;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSecurity {

    private TestRepository testRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private AttemptRepository attemptRepository;

    public UserSecurity(
        TestRepository testRepository,
        QuestionRepository questionRepository,
        AnswerRepository answerRepository,
        AttemptRepository attemptRepository
    ) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.attemptRepository = attemptRepository;
    }

    public boolean hasAccessToTest(Authentication authentication, Long testId) {
        FUserDetails userDetails = (FUserDetails) authentication.getPrincipal();
        Optional<Test> test = testRepository.findTestById(testId);
        return !test.isPresent() || userDetails.getUser().getId().equals(test.get().getUser().getId());
    }

    public boolean hasAccessToQuestion(Authentication authentication, Long questionId) {
        Optional<Question> question = questionRepository.findQuestionById(questionId);
        return !question.isPresent() || hasAccessToTest(authentication, question.get().getTest().getId());
    }

    public boolean hasAccessToAnswer(Authentication authentication, Long answerId) {
        Optional<Answer> answer = answerRepository.findAnswerById(answerId);
        return !answer.isPresent() || hasAccessToQuestion(authentication, answer.get().getQuestion().getId());
    }

    public boolean hasAccessToAttempt(Authentication authentication, Long attemptId) {
        FUserDetails userDetails = (FUserDetails) authentication.getPrincipal();
        Optional<Attempt> attempt = attemptRepository.findAttemptById(attemptId);
        return !attempt.isPresent() || userDetails.getUser().getId().equals(attempt.get().getUser().getId());
    }
}
