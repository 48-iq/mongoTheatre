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
import ru.ivanov.theatremongo.dto.IdDto;
import ru.ivanov.theatremongo.dto.TicketDto;
import ru.ivanov.theatremongo.service.PerformanceService;
import ru.ivanov.theatremongo.service.TicketService;



@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final PerformanceService performanceService;

    @GetMapping("/performances/{id}")
    public String getTicketsByPerformanceId(@PathVariable("id") String id, Model model) {
        model.addAttribute("tickets", ticketService.getTicketsByPerformanceId(id));
        model.addAttribute("performance", performanceService.getPerformanceById(id));
        return "ticket/all";
    }

    @GetMapping("performances/{id}/new")
    public String getNewTicketPage(Model model, @PathVariable("id") String id) {
        model.addAttribute("performance", performanceService.getPerformanceById(id));
        return "ticket/new";
    }
    
    @PostMapping("/performances/{id}")
    public String createTicket(@ModelAttribute TicketDto ticketDto, @PathVariable("id") String performanceId) {
        ticketDto.setId(null);
        ticketDto = ticketService.saveTicket(performanceId,ticketDto);
        performanceService.addTicketToPerformance(performanceId, ticketDto.getId());
        return "redirect:/tickets/performances/" + performanceId;
    }
    @PutMapping("/performances/{id}")
    public String updateTicket(@ModelAttribute TicketDto ticketDto, @PathVariable("id") String performanceId) {
        ticketDto = ticketService.saveTicket(performanceId,ticketDto);
        return "redirect:/tickets/performances/" + performanceId;
    }
    
    @GetMapping("/{id}/update/performances/{perfId}")
    public String getUpdateTicketPage(@PathVariable("id") String id, Model model, @PathVariable("perfId") String perfId) {
        model.addAttribute("ticket", ticketService.getTicketById(id));
        model.addAttribute("performance", performanceService.getPerformanceById(perfId));
        return "ticket/update";
    }

    @DeleteMapping("{id}/performances/{perfId}")
    public String deleteTicket(@PathVariable("id") String id, @PathVariable("perfId") String perfId) {
        ticketService.deleteTicket(id);
        performanceService.removeTicketFromPerformance(perfId, id);
        return "redirect:/tickets/performances/" + perfId;
    }


    
}
