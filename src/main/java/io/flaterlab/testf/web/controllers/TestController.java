package io.flaterlab.testf.web.controllers;

import io.flaterlab.testf.io.entity.Test;
import io.flaterlab.testf.io.dao.TestRepository;
import io.flaterlab.testf.web.exceptions.TestNotFoundException;
import io.flaterlab.testf.web.dto.request.TestRequestModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/v1/tests")
public class TestController {

    private TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("")
    public ResponseEntity all() {
        return ok(testRepository.findAll());
    }

    @PostMapping("")
    public ResponseEntity save(@RequestBody TestRequestModel model, HttpServletRequest request) {
        Test saved = testRepository.save(Test.builder().name(model.getName()).build());

        return created(
            ServletUriComponentsBuilder
                .fromContextPath(request)
                .path("tests/{it}")
                .buildAndExpand(saved.getId())
                .toUri()
        ).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        return ok(testRepository.findById(id).orElseThrow(() -> new TestNotFoundException(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, TestRequestModel model) {
        Test existed = testRepository.findById(id).orElseThrow(() -> new TestNotFoundException(id));
        existed.setName(model.getName());

        testRepository.save(existed);
        return noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Test existed = testRepository.findById(id).orElseThrow(() -> new TestNotFoundException(id));
        testRepository.delete(existed);
        return noContent().build();
    }

}
