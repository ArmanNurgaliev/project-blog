package com.arman.site.controllers;

import com.arman.site.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user,
                       Model model) {
        model.addAttribute("user", user);
        model.addAttribute("title", "Main page");
        return "home";
    }

    @GetMapping("/about")
    public String about(@AuthenticationPrincipal User user,
                        Model model) {

        model.addAttribute("user", user);
        model.addAttribute("text", "Page about us");

        return "about";
    }

}