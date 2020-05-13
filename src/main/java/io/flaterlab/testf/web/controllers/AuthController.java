package io.flaterlab.testf.web.controllers;

import io.flaterlab.testf.service.IUserService;
import io.flaterlab.testf.web.dto.request.SignInRequestDto;
import io.flaterlab.testf.web.dto.request.SignUpRequestDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity signinEncoded(final SignInRequestDto body) {
        return userService.signIn(body);
    }

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signinJson(@RequestBody final SignInRequestDto body) {
        return userService.signIn(body);
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity signupEncoded(final SignUpRequestDto signUpRequestDto) {
        return userService.signUp(signUpRequestDto);
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signupJson(@RequestBody final SignUpRequestDto signUpRequestDto) {
        return userService.signUp(signUpRequestDto);
    }
}
