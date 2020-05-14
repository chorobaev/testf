package io.flaterlab.testf.security;

import io.flaterlab.testf.persistence.dao.TestRepository;
import io.flaterlab.testf.persistence.model.Test;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSecurity {

    private TestRepository testRepository;

    public UserSecurity(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public boolean hasAccessToEditTest(Authentication authentication, Long testId) {
        FUserDetails userDetails = (FUserDetails) authentication.getPrincipal();
        System.out.println("Test id: " + testId);
        Optional<Test> test = testRepository.findTestById(testId);
        return test.isPresent() && userDetails.getUser().getId().equals(test.get().getUser().getId());
    }
}
