package com.arman.site.controllers;

import com.arman.site.models.User;
import com.arman.site.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/add/{post_id}")
    public String addComment(@AuthenticationPrincipal User user,
                             @PathVariable Long post_id,
                             @RequestParam String text,
                             Model model) {

        commentService.addComment(text, user, post_id);

        return "redirect:/blog/" + post_id;
    }

}
