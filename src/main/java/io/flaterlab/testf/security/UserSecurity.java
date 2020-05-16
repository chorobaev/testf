package io.flaterlab.testf.security;

import io.flaterlab.testf.persistence.dao.AnswerRepository;
import io.flaterlab.testf.persistence.dao.QuestionRepository;
import io.flaterlab.testf.persistence.dao.TestRepository;
import io.flaterlab.testf.persistence.model.Answer;
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

    public UserSecurity(TestRepository testRepository,
                        QuestionRepository questionRepository,
                        AnswerRepository answerRepository) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public boolean hasAccessToEditTest(Authentication authentication, Long testId) {
        FUserDetails userDetails = (FUserDetails) authentication.getPrincipal();
        Optional<Test> test = testRepository.findTestById(testId);
        return test.isPresent() && userDetails.getUser().getId().equals(test.get().getUser().getId());
    }

    public boolean hasAccessToEditQuestion(Authentication authentication, Long questionId) {
        Optional<Question> question = questionRepository.findQuestionById(questionId);
        return question.isPresent() && hasAccessToEditTest(authentication, question.get().getTest().getId());
    }

    public boolean hasAccessToEditAnswer(Authentication authentication, Long answerId) {
        Optional<Answer> answer = answerRepository.findAnswerById(answerId);
        return answer.isPresent() && hasAccessToEditQuestion(authentication, answer.get().getQuestion().getId());
    }
}
