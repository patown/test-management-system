package com.test.management.system.ui;

import com.test.management.system.entity.Step;
import com.test.management.system.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/")
public class StepUiController {
    @Autowired
    StepService stepService;

    @PostMapping("/deleteStep")
    public String deleteStep(@RequestParam("stepId") int id) {
        stepService.deleteStep(id);
        return "redirect:/showAllSteps";
    }

    @GetMapping("/showAllSteps")
    public String showAllSteps(Model model) {
        List<Step> allSteps = stepService.getAllSteps();
        model.addAttribute("allSteps", allSteps);
        Step step = new Step();
        model.addAttribute("step", step);
        return "steps-list";
    }

    @PostMapping("/saveStep")
    public String saveStep(@ModelAttribute("stepDescription") String stepDescription, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "steps-list";
        } else {
            Step step = new Step(stepDescription);
            stepService.addStep(step);
        }
        return "redirect:/showAllSteps";
    }


}
