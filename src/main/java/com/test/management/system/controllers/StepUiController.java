package com.test.management.system.controllers;

import com.test.management.system.entity.Step;
import com.test.management.system.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.SortedSet;

@Controller
@RequestMapping(path = "/")
public class StepUiController {
    @Autowired
    StepService stepService;

    @PostMapping("/deleteStep")
    public String deleteStep(@RequestParam("stepId") Long id) {
        stepService.deleteById(id);
        return "redirect:/showAllSteps";
    }

    @GetMapping("/showAllSteps")
    public String showAllSteps(Model model) {
        SortedSet<Step> allSteps = stepService.findAll();
        model.addAttribute("allSteps", allSteps);
        Step step = new Step();
        model.addAttribute("step", step);
        return "steps-list";
    }

    @PostMapping("/saveStep")
    public String saveStep(@ModelAttribute Step step, BindingResult bindingResult) {
        stepService.save(step);
        return "redirect:/showAllSteps";
    }

}
