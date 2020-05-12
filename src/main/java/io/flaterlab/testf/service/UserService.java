package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.dao.RoleRepository;
import io.flaterlab.testf.persistence.dao.UserRepository;
import io.flaterlab.testf.persistence.model.User;
import io.flaterlab.testf.security.jwt.JwtTokenProvider;
import io.flaterlab.testf.utils.Json;
import io.flaterlab.testf.web.dto.request.SignInDto;
import io.flaterlab.testf.web.dto.request.AccountDto;
import io.flaterlab.testf.web.error.UserAlreadyExistException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class UserService implements IUserService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(
        AuthenticationManager authenticationManager,
        JwtTokenProvider jwtTokenProvider,
        UserRepository userRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity signIn(SignInDto body) {
        try {
            String username = body.getUsername();
            String password = body.getPassword();
            System.out.println("Username: " + username + "; password: " + password);

            Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(auth);
            String token = jwtTokenProvider.createToken(username);

            return ok(Json.builder()
                .put("username", username)
                .put("token", token)
                .buildMap()
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @Override
    public ResponseEntity signUp(AccountDto accountDto) {
        if (userRepository.findByUsername(accountDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username " + accountDto.getUsername() + " already exists");
        }

        final User user = User.builder()
            .passwordHash(passwordEncoder.encode(accountDto.getPassword()))
            .roles(Collections.singletonList(roleRepository.findByName("ROLE_HOST").orElseThrow(() -> new IllegalArgumentException(""))))
            .registeredAt(new Date())
            .enabled(true)
            .build();

        BeanUtils.copyProperties(accountDto, user);
        System.out.println("User: " + user.getUsername() + "; Pass: " + user.getPasswordHash());
        User rd = userRepository.save(user);

        System.out.println("User r: " + rd.getUsername() + "; Pass r: " + rd.getPasswordHash());

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
