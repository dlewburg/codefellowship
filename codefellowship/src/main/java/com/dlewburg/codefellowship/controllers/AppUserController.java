package com.dlewburg.codefellowship.controllers;

import com.dlewburg.codefellowship.models.AppUser;
import com.dlewburg.codefellowship.repos.AppUserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class AppUserController {
    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/")
    public String getHomePage(Model m, Principal p) {

        if(p != null) {

            String username = p.getName();
            AppUser user = appUserRepo.findByUsername(username);

            m.addAttribute("username", username);
        }
        return "index.html";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        //
        return "login.html";
    }


    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup.html";
    }

    @PostMapping("/signup")
    public RedirectView createUser(String username, String password, String firstName, String lastName, String dateOfBirth, String bio) {
        AppUser newUser = new AppUser();
        newUser.setUsername(username);
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setDateOfBirth(dateOfBirth);
        newUser.setBio(bio);

        appUserRepo.save(newUser);
        authWithHttpServletRequest(username, password);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(newUser, newUser.getPassword(), newUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/");
    }

    public void authWithHttpServletRequest(String username, String password) {
        try{
            request.login(username, password);
        } catch(ServletException e) {
            System.out.println("Error While Logging In");
            e.printStackTrace();
        }
    }

    @GetMapping("/user/{id}")
    public String getUserInfoPage(Model m, Principal p, @PathVariable long id) {
        if(p != null) { // ensures someone is logged in; not needed if web security config is set up properly
            String username = p.getName();
            AppUser  browsingUser = appUserRepo.findByUsername(username);
            m.addAttribute("username", browsingUser.getUsername());
        }

        AppUser profileUser = appUserRepo.findById(id).orElseThrow();
        m.addAttribute("profileFirstName", profileUser.getFirstName());
        m.addAttribute("profileLastName", profileUser.getLastName());
        m.addAttribute("profileBio", profileUser.getBio());
        m.addAttribute("profileDateOfBirth", profileUser.getDateOfBirth());


        return "myprofile.html";
    }


}
