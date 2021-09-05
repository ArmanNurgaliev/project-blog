package com.arman.site.controllers;

import com.arman.site.models.User;
import com.arman.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{user_id}")
    public String profile(Principal principal,
                            @PathVariable Long user_id, Model model) {
        User user = userService.getUserById(user_id);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", userService.getUser(principal));

        return "profile";
    }

    @GetMapping("/edit")
    public String editProfile(Principal principal,
                              Model model) {
        model.addAttribute("user", userService.getUser(principal));

        return "profile-edit";
    }

    @PostMapping("/edit")
    public String updateProfile(Principal principal,
                              @RequestParam(required = false) String username,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String about) {
        User user = userService.getUser(principal);
        userService.updateProfile(user.getId(), username, email, about);

        return "redirect:/profile/" + user.getId();
    }
}
