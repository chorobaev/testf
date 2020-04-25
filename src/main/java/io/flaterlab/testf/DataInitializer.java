package io.flaterlab.testf;

import io.flaterlab.testf.io.entity.User;
import io.flaterlab.testf.io.repositories.TestRepository;
import io.flaterlab.testf.io.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private TestRepository testRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DataInitializer(
        TestRepository testRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
//        log.debug("initializing test data...");
//        Arrays.asList("Math", "English").forEach(v -> testRepository.saveAndFlush(Test.builder().name(v).build()));
//        log.debug("printing all tests...");
//        testRepository.findAll().forEach(v -> log.debug(" Test :" + v.toString()));
//        userRepository.save(User.builder()
//            .username("user")
//            .password(this.passwordEncoder.encode("password"))
//            .roles(Collections.singletonList("ROLE_USER"))
//            .build()
//        );
//        userRepository.save(User.builder()
//            .username("admin")
//            .password(this.passwordEncoder.encode("password"))
//            .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
//            .build()
//        );
//
//        log.debug("printing all users...");
//        userRepository.findAll().forEach(v -> log.debug(" User :" + v.toString()));
    }
}