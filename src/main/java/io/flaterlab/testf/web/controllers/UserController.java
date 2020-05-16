package io.flaterlab.testf.web.controllers;

import io.flaterlab.testf.security.FUserDetails;
import io.flaterlab.testf.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/profile")
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public ResponseEntity getProfileInfo(Authentication auth) {
        FUserDetails userDetails = (FUserDetails) auth.getPrincipal();
        return userService.getProfileInfo(userDetails.getUser());
    }
}
