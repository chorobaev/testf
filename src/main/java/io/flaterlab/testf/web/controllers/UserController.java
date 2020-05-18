package io.flaterlab.testf.web.controllers;

import io.flaterlab.testf.security.FUserDetails;
import io.flaterlab.testf.service.IUserService;
import io.flaterlab.testf.web.dto.request.AccountInfoRequestDto;
import io.flaterlab.testf.web.dto.request.SignUpRequestDto;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/account")
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public ResponseEntity getAccountInfo(Authentication auth) {
        FUserDetails userDetails = (FUserDetails) auth.getPrincipal();
        return userService.getAccountInfo(userDetails.getUser());
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateAccountInfoEncoded(Authentication auth, AccountInfoRequestDto body) {
        FUserDetails userDetails = (FUserDetails) auth.getPrincipal();
        return userService.updateAccountInfo(userDetails.getUser(), body);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateAccountInfoJson(Authentication auth, @RequestBody AccountInfoRequestDto body) {
        FUserDetails userDetails = (FUserDetails) auth.getPrincipal();
        return userService.updateAccountInfo(userDetails.getUser(), body);
    }

}
