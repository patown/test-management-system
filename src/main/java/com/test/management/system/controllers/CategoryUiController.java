package com.test.management.system.controllers;

import com.test.management.system.FileService.FileService;
import com.test.management.system.wrappers.TestWrapper;
import com.test.management.system.entity.Category;
import com.test.management.system.entity.Test;
import com.test.management.system.service.CategoryService;
import com.test.management.system.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/")
public class CategoryUiController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    TestService testService;

    @Autowired
    FileService fileService;

    @PostMapping("/deleteCat")
    public String deleteCategory(@RequestParam("categoryId") Long id) {
        categoryService.deleteById(id);
        return "redirect:/showAllCat";
    }

    @GetMapping("/showAllCat")
    public String showAllCategories(Model model) {
        SortedSet<Category> all = categoryService.findAll();
        model.addAttribute("allCategories", all);
        Category category = new Category();
        model.addAttribute("category", category);
        return "categories-list";
    }

    @PostMapping("/saveCat")
    public String saveCat(@ModelAttribute Category category, BindingResult bindingResult) {
        categoryService.save(category);
        return "redirect:/showAllCat";
    }

    @PostMapping("/generateFeatureFile")
    public String generate(@RequestParam("categoryId") Long categoryId, Model model) {
        Category category = categoryService.findById(categoryId);
        Set<Test> allTests = testService.findAll();
        List<Test> result = allTests.stream().filter(s -> s.getCategory().equals(category) && !s.getSteps().isEmpty()).collect(Collectors.toList());
        TestWrapper wrapper = new TestWrapper();
        wrapper.setList(result);
        wrapper.setFeatureName(category.getCategoryName());
        model.addAttribute("wrapper", wrapper);
        return "download.html";
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<InputStreamResource> createFile(
            @ModelAttribute("wrapper") TestWrapper testWrapper)
            throws IOException {
        File file = fileService.createFeatureFile(testWrapper);
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.TEXT_PLAIN);
        respHeaders.setContentDispositionFormData("attachment", testWrapper.getFeatureName() + ".feature");
        InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
        return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
    }


}
