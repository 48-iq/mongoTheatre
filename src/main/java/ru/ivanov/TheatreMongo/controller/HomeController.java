package ru.ivanov.theatremongo.controller;

import lombok.RequiredArgsConstructor;
import ru.ivanov.theatremongo.service.PerformanceService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    private final PerformanceService performanceService;
    @GetMapping
    public String homePage(Model model) {
        return "home/home";
    }

}
