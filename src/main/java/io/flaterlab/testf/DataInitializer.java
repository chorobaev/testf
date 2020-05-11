package io.flaterlab.testf;

import io.flaterlab.testf.persistence.dao.UserRepository;
import io.flaterlab.testf.persistence.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DataInitializer(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
//        userRepository.save(User.builder()
//            .firstName("Patric")
//            .username("user")
//            .passwordHash(this.passwordEncoder.encode("password"))
//            .registeredAt(new Date())
//            .roles(Arrays.asList("ROLE_USER", "ROLE_HOST"))
//            .build()
//        );
//        userRepository.save(User.builder()
//            .firstName("Tom")
//            .username("admin")
//            .passwordHash(this.passwordEncoder.encode("password"))
//            .registeredAt(new Date())
//            .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
//            .build()
//        );
//
//        log.debug("printing all users...");
//        userRepository.findAll().forEach(v -> log.debug(" User :" + v.toString()));
    }
}