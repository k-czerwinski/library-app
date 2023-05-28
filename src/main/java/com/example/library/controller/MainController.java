package com.example.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    @RequestMapping("/")
    public String displayMainMenu() {
        return "main-menu";
    }

    @RequestMapping("/accessDenied")
    public String accessDenied(){
        return "access-denied";
    }
}