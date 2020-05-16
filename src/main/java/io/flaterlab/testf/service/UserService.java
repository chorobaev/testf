package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.dao.RoleRepository;
import io.flaterlab.testf.persistence.dao.UserRepository;
import io.flaterlab.testf.persistence.model.User;
import io.flaterlab.testf.security.jwt.JwtTokenProvider;
import io.flaterlab.testf.utils.Json;
import io.flaterlab.testf.web.dto.request.SignInRequestDto;
import io.flaterlab.testf.web.dto.request.SignUpRequestDto;
import io.flaterlab.testf.web.dto.response.ProfileResponseDto;
import io.flaterlab.testf.web.error.UserAlreadyExistException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

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
    public ResponseEntity signIn(SignInRequestDto body) {
        try {
            String username = body.getUsername();
            String password = body.getPassword();

            Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(auth);
            String token = jwtTokenProvider.createToken(username);

            return ok(Json.builder()
                .put("username", username)
                .put("token", token)
                .buildMap()
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity signUp(SignUpRequestDto signUpRequestDto) {
        if (userRepository.findByUsername(signUpRequestDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username '" + signUpRequestDto.getUsername() + "' already exists");
        }

        final User user = User.builder()
            .passwordHash(passwordEncoder.encode(signUpRequestDto.getPassword()))
            // TODO: change ROLE_USER to ROLE_HOST in release
            .roles(Collections.singletonList(roleRepository.findByName("ROLE_HOST").orElseThrow(() -> new IllegalArgumentException(""))))
            .registeredAt(new Date())
            .enabled(true)
            .build();

        BeanUtils.copyProperties(signUpRequestDto, user);
        userRepository.save(user);

        return signIn(SignInRequestDto.builder()
            .username(signUpRequestDto.getUsername())
            .password(signUpRequestDto.getPassword())
            .build());
    }

    @Override
    public ResponseEntity getProfileInfo(User user) {
        ProfileResponseDto responseDto = new ProfileResponseDto();
        BeanUtils.copyProperties(user, responseDto);
        return ok(responseDto);
    }
}
