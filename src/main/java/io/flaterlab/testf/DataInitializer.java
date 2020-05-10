package io.flaterlab.testf;

import io.flaterlab.testf.io.dao.TestRepository;
import io.flaterlab.testf.io.dao.UserRepository;
import io.flaterlab.testf.io.entity.Test;
import io.flaterlab.testf.io.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

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
        userRepository.save(User.builder()
            .firstName("Patric")
            .username("user")
            .passwordHash(this.passwordEncoder.encode("password"))
            .registeredAt(new Date())
            .roles(Collections.singletonList("ROLE_USER"))
            .build()
        );
        userRepository.save(User.builder()
            .firstName("Tom")
            .username("admin")
            .passwordHash(this.passwordEncoder.encode("password"))
            .registeredAt(new Date())
            .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
            .build()
        );

        log.debug("printing all users...");
        userRepository.findAll().forEach(v -> log.debug(" User :" + v.toString()));
    }
}