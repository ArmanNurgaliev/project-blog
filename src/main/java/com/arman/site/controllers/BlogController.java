package com.arman.site.controllers;

import com.arman.site.models.*;
import com.arman.site.repository.SubCommentRepository;
import com.arman.site.service.CommentService;
import com.arman.site.service.PostService;
import com.arman.site.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BlogController {
    @Value("${upload.path}")
    private String path;
    private StorageService storageService;
    private PostService postService;
    private CommentService commentService;
    private SubCommentRepository subCommentRepository;

    @Autowired
    public BlogController(StorageService storageService, PostService postService, CommentService commentService, SubCommentRepository subCommentRepository) {
        this.storageService = storageService;
        this.postService = postService;
        this.commentService = commentService;
        this.subCommentRepository = subCommentRepository;
    }

    @GetMapping("/blog")
    public String blogMain(@AuthenticationPrincipal User user,
                           Model model) {
        Iterable<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(@AuthenticationPrincipal User user,
                          Model model) {
        model.addAttribute("user", user);

        return "blog-add";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/blog/add")
    public String blogPostAdd(@AuthenticationPrincipal User user,
                              @RequestParam MultipartFile[] files,
                              @RequestParam String title,
                              @RequestParam String anons,
                              @RequestParam String full_text,
                              Model model) throws IOException {
     //   model.addAttribute("user", user);
        postService.addPost(title, anons, full_text, user);

        storageService.store(files, title);

        return "redirect:/blog";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/blog/{post_id}")
    public String blogDetails(@PathVariable Long post_id,
                              @AuthenticationPrincipal User user,
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

        model.addAttribute("files", files);
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("subComments", subComments);

        return "blog-details";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/blog/{post_id}/edit")
    public String blogEdit(@PathVariable Long post_id,
                           @AuthenticationPrincipal User user,
                           Model model) {

        Post post = postService.findById(post_id);
        if(post == null)
            return "redirect:/blog";

        List<FileDB> files = storageService.load(post_id);

        model.addAttribute("files", files);
        model.addAttribute("user", user);
        model.addAttribute("post", post);

        return "blog-edit";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/blog/{post_id}/edit")
    public String blogPostEdit(@PathVariable Long post_id,
                               @RequestParam String title,
                               @RequestParam String anons,
                               @RequestParam String full_text,
                               @RequestParam MultipartFile[] files,
                               @AuthenticationPrincipal User user,
                              Model model) throws IOException {

        postService.editPost(post_id, title, anons, full_text);
        storageService.store(files, title);

        return "redirect:/blog/" + post_id;
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/blog/{post_id}/remove")
    public String blogPostDelete(@PathVariable long post_id,
                               Model model) {

        postService.delete(post_id);

        return "redirect:/blog";
    }
}
