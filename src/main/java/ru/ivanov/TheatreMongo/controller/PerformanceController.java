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
import ru.ivanov.theatremongo.dto.NameDto;
import ru.ivanov.theatremongo.dto.PerformanceDto;
import ru.ivanov.theatremongo.service.PerformanceService;




@Controller
@RequiredArgsConstructor
@RequestMapping("/performances")
public class PerformanceController {
    private final PerformanceService performanceService;
    @GetMapping("/income")
    public String getIncome(Model model) {
        model.addAttribute("income", performanceService.getIncome());
        return "performance/income";
    }

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

    @PutMapping
    public String updatePerformanceProcessing(@ModelAttribute PerformanceDto performanceDto) {
        performanceDto = performanceService.savePerformance(performanceDto);
        return String.format("redirect:/performances/%s/update", performanceDto.getId());
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

    @GetMapping("/find")
    public String getMethodName(Model model, @ModelAttribute NameDto name) {
        if (name == null || name.toString().isBlank()) {
            return "redirect:/performances";
        }
        model.addAttribute("performances", performanceService.getPerformancesByName(name.toString()));
        return "performance/all";
    }
    
    
}
