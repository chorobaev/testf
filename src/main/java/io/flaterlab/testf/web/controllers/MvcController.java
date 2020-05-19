package io.flaterlab.testf.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcController {

    @GetMapping("/")
    public String landingPage() {
        return "index";
    }
}
