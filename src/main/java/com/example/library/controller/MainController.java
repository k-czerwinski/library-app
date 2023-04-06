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

    @RequestMapping("/redirectTo")
    public String afterLogin(HttpServletRequest httpServletRequest){
        if(httpServletRequest.isUserInRole("ADMIN"))
            return "redirect:/book-manager/";
        return "redirect:/library/";
    }

    @RequestMapping("/accessDenied")
    public String accessDenied(){
        return "access-denied";
    }
}