package io.flaterlab.testf.web.controllers;

import io.flaterlab.testf.persistence.dao.UserRepository;
import io.flaterlab.testf.persistence.model.User;
import io.flaterlab.testf.security.jwt.JwtTokenProvider;
import io.flaterlab.testf.service.IUserService;
import io.flaterlab.testf.service.UserService;
import io.flaterlab.testf.web.dto.request.SignInDto;
import io.flaterlab.testf.web.dto.request.SignUpDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private IUserService userService;

    public AuthController(
        IUserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity signinEncoded(final SignInDto body) {
        return userService.signIn(body);
    }

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signinJson(@RequestBody final SignInDto body) {
        return userService.signIn(body);
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity signupEncoded(final SignUpDto accountDto) {
        return userService.signUp(accountDto);
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signupJson(@RequestBody final SignUpDto accountDto) {
        return userService.signUp(accountDto);
    }
}
