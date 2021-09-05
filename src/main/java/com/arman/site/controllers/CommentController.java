package com.arman.site.controllers;

import com.arman.site.models.User;
import com.arman.site.service.CommentService;
import com.arman.site.service.SubCommentService;
import com.arman.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/comment")
public class CommentController {
    private CommentService commentService;
    private SubCommentService subCommentService;
    private UserService userService;

    @Autowired
    public CommentController(CommentService commentService, SubCommentService subCommentService, UserService userService) {
        this.commentService = commentService;
        this.subCommentService = subCommentService;
        this.userService = userService;
    }

    @PostMapping("/add/{post_id}")
    public String addComment(Principal principal,
                             @PathVariable Long post_id,
                             @RequestParam String text,
                             Model model) {
        User user = userService.getUser(principal);
        commentService.addComment(text, user, post_id);

        return "redirect:/blog/" + post_id;
    }

    @PostMapping("/reply/{post_id}/{comment_id}")
    public String replyComment(Principal principal,
                               @PathVariable Long post_id,
                               @PathVariable Long comment_id,
                               @RequestParam String text,
                               Model model) {
        User user = userService.getUser(principal);
        subCommentService.replyComment(text, user, comment_id);

        return "redirect:/blog/" + post_id;
    }

}
