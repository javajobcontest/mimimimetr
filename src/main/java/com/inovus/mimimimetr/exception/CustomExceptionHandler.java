package com.inovus.mimimimetr.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public String handleCustom(Model model, CustomException e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "index";
    }

    @ExceptionHandler(Exception.class)
    public String handleAll(Model model, Exception e) {
        model.addAttribute("errorMessage", "Произошла ошибка, попробуйте еще раз");
        return "index";
    }

}
