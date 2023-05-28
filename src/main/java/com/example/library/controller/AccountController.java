package com.example.library.controller;

import com.example.library.exceptions.AlreadyExistException;
import com.example.library.exceptions.PasswordsNotMatchingException;
import com.example.library.model.User;
import com.example.library.model.UserDTO;
import com.example.library.service.AccountService;
import com.example.library.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;

    @Autowired
    public AccountController(AccountService accountService, CustomerService customerService){
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @GetMapping("/registerForm")
    public String displayRegisterForm(Model model){
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        return "accountController/register-form";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid UserDTO user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) return "accountController/register-form";
        try{
            accountService.saveUser(user);
            model.addAttribute("message", "You have successfully registered. You can now sing in.");
        }
        catch (AlreadyExistException e){
            model.addAttribute("message", "User with that email already exist");
        }
        catch (PasswordsNotMatchingException e){
            model.addAttribute("matchingPasswordError", "Password not matching");
        }
        return "accountController/register-form";
    }

    @GetMapping("/login")
    public String singinForm(){
        return "accountController/login";
    }

    @PostMapping("/loginWithError")
    public String singInWithError(Model model){
        model.addAttribute("errorMessage", "Username or password is incorrect");
        return "accountController/login";
    }

    @GetMapping("/logoutPage")
    public String logout(){
        customerService.clearCart();
        customerService.clearBooksBorrowed();
        return "accountController/logout";
    }

    @RequestMapping("/redirectTo")
    public String afterLogin(HttpServletRequest httpServletRequest, Authentication authentication){
        if(httpServletRequest.isUserInRole("ADMIN")){
            return "redirect:/book-manager/";
        }
        customerService.loadBorrowedBooks(authentication.getName());
        return "redirect:/library/";
    }
}