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
import ru.ivanov.theatremongo.dto.ActorDto;
import ru.ivanov.theatremongo.dto.NameDto;
import ru.ivanov.theatremongo.service.ActorService;
import ru.ivanov.theatremongo.service.PerformanceService;



@Controller
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorsController {
    private final ActorService actorService;
    private final PerformanceService performanceService;

    @GetMapping
    public String getAllActors(Model model) {
        model.addAttribute("actors", actorService.getAllActors());
        return "actor/all";
    }

    @GetMapping("/performances/{id}")
    public String getPerformanceActors(@PathVariable("id") String id, Model model) {
        model.addAttribute("actors", actorService.getPerformanceActors(id));
        model.addAttribute("performance", performanceService.getPerformanceById(id));
        return "actor/performance";
    }
    

    @GetMapping("/{id}/update")
    public String getActorUpdatePage(@PathVariable("id") String id, Model model) {
        model.addAttribute("actor", actorService.getActorById(id));
        return "actor/update";
    }
    @GetMapping("/new")
    public String getMethodName() {
        return "actor/new";
    }
    

    @GetMapping("/performances/{id}/add")
    public String getPerformanceAddActorsPage(@PathVariable("id") String id, Model model) {
        model.addAttribute("actors", actorService.getActorsWithoutPerformance(id));
        model.addAttribute("performance", performanceService.getPerformanceById(id));
        return "actor/add-to-performance";
    }

    @PostMapping("/{id}/addto/performances/{perfId}")
    public String addActorToPerformance(@PathVariable("id") String actorId, @PathVariable("perfId") String performanceId) {
        performanceService.addActorToPerformance(performanceId, actorId);
        return String.format("redirect:/actors/performances/%s/add", performanceId);
    }
    
    @PostMapping
    public String createActor(@ModelAttribute ActorDto actorDto) {
        actorService.saveActor(actorDto);
        return "redirect:/actors";
    }

    @PutMapping
    public String updateActor(@ModelAttribute ActorDto actorDto) {
        actorService.updateActor(actorDto);
        return "redirect:/actors";
    }

    @DeleteMapping("/{id}")
    public String deleteActor(@PathVariable("id") String id) {
        actorService.deleteActor(id);
        return "redirect:/actors";
    }

    @DeleteMapping("/{id}/from/performances/{perfId}")
    public String deleteActorFromPerformance(@PathVariable("id") String actorId, @PathVariable("perfId") String performanceId) {
        performanceService.removeActorFromPerformance(performanceId, actorId);
        return String.format("redirect:/actors/performances/%s", performanceId);
    }

    @GetMapping("/find")
    public String getMethodName(Model model, @ModelAttribute NameDto name) {
        if (name == null || name.getName().isBlank()) {
            return "redirect:/actors";
        }
        model.addAttribute("actors", actorService.getActorsByName(name.getName()));
        return "actor/all";
    }
    

}
