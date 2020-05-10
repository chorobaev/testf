package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.dao.UserRepository;
import io.flaterlab.testf.persistence.model.User;
import io.flaterlab.testf.security.jwt.JwtTokenProvider;
import io.flaterlab.testf.utils.Json;
import io.flaterlab.testf.web.dto.request.SignInDto;
import io.flaterlab.testf.web.dto.request.SignUpDto;
import io.flaterlab.testf.web.error.UserAlreadyExistException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class UserService implements IUserService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(
        AuthenticationManager authenticationManager,
        JwtTokenProvider jwtTokenProvider,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity signIn(SignInDto body) {
        try {
            String username = body.getUsername();
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, body.getPassword())
            );
            String token = jwtTokenProvider.createToken(username, findUserByUsername(username).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @Override
    public ResponseEntity signUp(SignUpDto accountDto) {
        if (userRepository.findByUsername(accountDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username " + accountDto.getUsername() + " already exists");
        }

        final User user = User.builder()
            .passwordHash(passwordEncoder.encode(accountDto.getPassword()))
            .roles(Arrays.asList("ROLE_USER", "ROLE_HOST"))
            .registeredAt(new Date())
            .build();

        BeanUtils.copyProperties(accountDto, user);
        userRepository.save(user);

        return signIn(SignInDto.builder()
            .username(accountDto.getUsername())
            .password(accountDto.getPassword())
            .build());
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException(""));
    }
}
