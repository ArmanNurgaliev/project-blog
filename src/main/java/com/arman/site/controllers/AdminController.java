package com.arman.site.controllers;

import com.arman.site.models.Role;
import com.arman.site.models.User;
import com.arman.site.repository.UserRepository;
import com.arman.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String adminPage(@AuthenticationPrincipal User user,
                            Model model) {
        model.addAttribute("user", user);
        model.addAttribute("users", userRepository.findAll());

        return "admin";
    }

    @GetMapping("/{user_id}")
    public String updateUser(@AuthenticationPrincipal User user,
                            @PathVariable Long user_id,
                             Model model) {

        model.addAttribute("user", user);
        model.addAttribute("usr", userRepository.findById(user_id).orElse(null));
        model.addAttribute("roles", Role.values());

        return "admin-update";
    }

    @PostMapping("/{user_id}")
    public String updateUser(
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String email,
                             @RequestParam Map<String, String> form,
                             @PathVariable Long user_id) {

        userService.updateUser(user_id, name, email, form);

        return "redirect:/admin";
    }
}
