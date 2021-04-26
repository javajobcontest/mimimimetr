package com.inovus.mimimimetr.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handle(Model model, Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "index";
    }
}
