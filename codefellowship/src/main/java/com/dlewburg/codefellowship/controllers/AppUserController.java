package com.dlewburg.codefellowship.controllers;

import com.dlewburg.codefellowship.repos.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppUserController {
    @Autowired
    AppUserRepo appUserRepo;

    @GetMapping("/")
    public String getHomePage(){
        return "index.html";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login.html";
    }

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup.html";
    }
}
