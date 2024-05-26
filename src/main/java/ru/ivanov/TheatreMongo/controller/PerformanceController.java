package ru.ivanov.theatremongo.controller;

import lombok.RequiredArgsConstructor;
import ru.ivanov.theatremongo.service.PerformanceService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceController {
    private final PerformanceService performanceService;

    @GetMapping
    public String getAllPerformance(Model model) {
        model.addAttribute("performances", performanceService.getAllPerformances());
        return "home/performances";
    }

    @GetMapping("/{id}")
    public String getPerformanceById(@PathVariable("id") String id, Model model) {
        model.addAttribute("performance", performanceService.getPerformanceById(id));
        return "home/performance";
    }
}
