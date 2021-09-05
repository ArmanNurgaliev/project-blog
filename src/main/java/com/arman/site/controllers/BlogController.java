package com.arman.site.controllers;

import com.arman.site.models.*;
import com.arman.site.repository.SubCommentRepository;
import com.arman.site.service.CommentService;
import com.arman.site.service.PostService;
import com.arman.site.service.UserService;
import com.arman.site.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blog")
public class BlogController {
    private StorageService storageService;
    private PostService postService;
    private CommentService commentService;
    private SubCommentRepository subCommentRepository;
    private UserService userService;

    @Autowired
    public BlogController(StorageService storageService, PostService postService, CommentService commentService, SubCommentRepository subCommentRepository, UserService userService) {
        this.storageService = storageService;
        this.postService = postService;
        this.commentService = commentService;
        this.subCommentRepository = subCommentRepository;
        this.userService = userService;
    }

    @GetMapping("")
    public String blogMain(Principal principal,
                           @RequestParam(required = false, defaultValue = "") String filter,
                           Model model) {
        Iterable<Post> posts;
        if (filter != null && !filter.isEmpty())
            posts = postService.findAllByTitle(filter);
        else
            posts = postService.findAll();
        User user = userService.getUser(principal);
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        return "blog-main";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/add")
    public String blogAdd(Principal principal,
                          Model model) {
        User user = userService.getUser(principal);
        model.addAttribute("user", user);
        model.addAttribute("post", new Post());
        return "blog-add";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/add")
    public String blogPostAdd(Principal principal,
                              @RequestParam MultipartFile[] files,
                              @RequestParam String title,
                              @RequestParam String anons,
                              @RequestParam String full_text,
                              Model model) throws IOException {
        Map<String, String> postErrors = postService.validatePost(title, anons, full_text);
        if (!postErrors.isEmpty()) {
            for (String s: postErrors.keySet())
                model.addAttribute(s, postErrors.get(s));
            return "blog-add";
        }
        User user = userService.getUser(principal);
        postService.addPost(title, anons, full_text, user);

        storageService.store(files, title);

        return "redirect:/blog";
    }

    @GetMapping("/{post_id}")
    public String blogDetails(@PathVariable Long post_id,
                              Principal principal,
                              Model model) {
        /*if (!postRepository.existsById(id))
            return "redirect:/blog";
*/
        Post post = postService.findById(post_id);
        if(post == null)
            return "redirect:/blog";
        List<Comment> comments = commentService.findAllByPost_id(post_id);
        List<SubComment> subComments = new ArrayList<>();
        for (Comment comment: comments) {
            subComments.addAll(subCommentRepository.findAllByParent_id(comment.getId()));
        }
        List<FileDB> files = storageService.load(post_id);

        User user = userService.getUser(principal);
        model.addAttribute("files", files);
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("subComments", subComments);
        model.addAttribute("admin", Role.ADMIN);

        return "blog-details";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{post_id}/edit")
    public String blogEdit(@PathVariable Long post_id,
                           Principal principal,
                           Model model) {

        Post post = postService.findById(post_id);
        if(post == null)
            return "redirect:/blog";

        List<FileDB> files = storageService.load(post_id);
        User user = userService.getUser(principal);
        model.addAttribute("files", files);
        model.addAttribute("user", user);
        model.addAttribute("post", post);

        return "blog-edit";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{post_id}/edit")
    public String blogPostEdit(@PathVariable Long post_id,
                               @RequestParam String title,
                               @RequestParam String anons,
                               @RequestParam String full_text,
                               @RequestParam MultipartFile[] files,
                              Model model) throws IOException {


        postService.editPost(post_id, title, anons, full_text);
        storageService.store(files, title);

        return "redirect:/blog/" + post_id;
    }


    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{post_id}/remove")
    public String blogPostDelete(@PathVariable long post_id,
                               Model model) {

        postService.delete(post_id);

        return "redirect:/blog";
    }
}
