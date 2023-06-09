package com.example.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(value = Exception.class )
    public String handleException(HttpServletRequest request, Exception ex, Model model) {
        LOGGER.error("Request "+  request.getRequestURI()+ " threw an exception: " + ex);
        model.addAttribute("error", ex);
        return "/exceptionController/general-exception";
    }
}