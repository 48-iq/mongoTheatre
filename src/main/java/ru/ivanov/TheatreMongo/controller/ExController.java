package ru.ivanov.theatremongo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExController {
    @ExceptionHandler
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e);
        return "error/error";
    }
}
