package com.test.management.system.controller.rest;

import com.test.management.system.entity.Step;
import com.test.management.system.entity.Test;
import com.test.management.system.service.StepService;
import com.test.management.system.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class TestRestController {

    private TestService testService;
    private StepService stepService;

    public TestRestController(TestService testService, StepService stepService) {
        this.testService = testService;
        this.stepService = stepService;
    }

    @GetMapping("/tests")
    public List<Test> getTests() {
        return testService.findAll();
    }


    @GetMapping("/tests/{testId}")
    public Test getTest(@PathVariable Long testId) {
        return testService.findById(testId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/tests")
    public Test addTest(@RequestBody Test test) {
        testService.save(test);
        return test;
    }

    @PutMapping("/tests")
    public Test updateTest(@RequestBody Test test) {
        testService.save(test);
        return test;

    }

    @GetMapping("/tests/{testId}/steps")
    public List<Step> getStepsForTest(@PathVariable Long testId) {
        return testService.getSteps(testId);
    }

    @PostMapping("/tests/{testId}/steps/{stepId}")
    public Test addStepToTest(@PathVariable Long testId, @PathVariable Long stepId) {
        Test test = testService.findById(testId);
        Step step = stepService.findById(stepId);
        test.addStep(step);
        testService.save(test);
        return test;
    }

    @ResponseStatus(HttpStatus.GONE)
    @DeleteMapping("/tests/{testId}")
    public String deleteTest(@PathVariable Long testId) {
        testService.deleteById(testId);
        return "Test " + testId + " was deleted";
    }

    @DeleteMapping("/tests/{testId}/steps/{stepId}")
    public String deleteStepFromTest(@PathVariable Long testId, @PathVariable Long stepId) {
        Test test = testService.findById(testId);
        Step step = stepService.findById(stepId);
        test.removeStep(step);
        testService.save(test);
        return String.format("Step %1$s was unlinked from test %2$s", stepId, testId);
    }

}