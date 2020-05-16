package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.model.User;
import io.flaterlab.testf.web.dto.request.SignInRequestDto;
import io.flaterlab.testf.web.dto.request.SignUpRequestDto;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    ResponseEntity signIn(final SignInRequestDto body);

    ResponseEntity signUp(final SignUpRequestDto signUpRequestDto);

    ResponseEntity getProfileInfo(User user);
}
