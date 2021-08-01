package com.arman.site.controllers;

import com.arman.site.models.User;
import com.arman.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{user_id}")
    public String profile(@PathVariable Long user_id, Model model) {
        User user = userService.getUserById(user_id);

        model.addAttribute(user);
        return "profile";
    }

    @GetMapping("/edit")
    public String editProfile(@AuthenticationPrincipal User user,
                              Model model) {
        model.addAttribute(user);

        return "profile-edit";
    }

    @PostMapping("/edit")
    public String updateProfile(@AuthenticationPrincipal User user,
                              @RequestParam String username,
                              @RequestParam String email,
                              @RequestParam String about) {

        userService.updateProfile(user.getId(), username, email, about);

        return "redirect:/profile/" + user.getId();
    }
}
