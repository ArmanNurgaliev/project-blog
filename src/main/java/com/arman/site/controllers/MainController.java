package com.arman.site.controllers;

import com.arman.site.models.User;
import com.arman.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {
    private UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Principal principal,
                       Model model) {
        User user = userService.getUser(principal);
        model.addAttribute("user", user);
        model.addAttribute("title", "Main page");
        return "home";
    }

    @GetMapping("/about")
    public String about(Principal principal,
                        Model model) {
        User user = userService.getUser(principal);
        model.addAttribute("user", user);
        model.addAttribute("text", "Page about us");

        return "about";
    }

}