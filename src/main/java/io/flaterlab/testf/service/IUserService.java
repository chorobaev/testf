package io.flaterlab.testf.service;

import io.flaterlab.testf.persistence.model.User;
import io.flaterlab.testf.web.dto.request.SignInDto;
import io.flaterlab.testf.web.dto.request.AccountDto;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    ResponseEntity signIn(final SignInDto body);

    ResponseEntity signUp(final AccountDto accountDto);

    User findUserByUsername(final String username);
}
