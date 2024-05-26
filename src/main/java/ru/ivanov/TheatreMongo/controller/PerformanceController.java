package ru.ivanov.theatremongo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ru.ivanov.theatremongo.dto.PerformanceDto;
import ru.ivanov.theatremongo.service.PerformanceService;



@Controller
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceController {
    private final PerformanceService performanceService;

    @GetMapping
    public String getAllPerformance(Model model) {
        model.addAttribute("performances", performanceService.getAllPerformances());
        return "performance/all";
    }

    @GetMapping("/{id}/update")
    public String getPerformanceById(@PathVariable("id") String id, Model model) {
        model.addAttribute("performance", performanceService.getPerformanceById(id));
        return "performance/update";
    }

    @GetMapping("/new")
    public String getPerformanceCreatePage() {
        return "performance/new";
    }

    @PutMapping("/{id}")
    public String updatePerformanceProcessing(@PathVariable("id") String id, @ModelAttribute PerformanceDto performanceDto) {
        performanceDto.setId(id);
        performanceService.savePerformance(performanceDto);
        return String.format("redirect:/performances/%s/update", id);
    }

    @PostMapping
    public String createPerformance(@ModelAttribute PerformanceDto performanceDto ) {
        performanceService.savePerformance(performanceDto);
        return "redirect:/performances";
    }

    @DeleteMapping("/{id}")
    public String deletePerformance(@PathVariable String id) {
        performanceService.deletePerformance(id);
        return "redirect:/performances";
    }
    
}
