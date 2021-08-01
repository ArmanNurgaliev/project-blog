package com.arman.site.controllers;

import com.arman.site.models.User;
import com.arman.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<User> users = userService.getAll();

        model.addAttribute("users", users);

        return "admin";
    }
}
