package com.arman.site.controllers;

import com.arman.site.models.FileDB;
import com.arman.site.models.Post;
import com.arman.site.models.User;
import com.arman.site.repository.FileRepository;
import com.arman.site.repository.PostRepository;
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
import java.util.List;

@Controller
public class BlogController {
    @Value("${upload.path}")
    private String path;
    private PostRepository postRepository;
    private FileRepository fileRepository;
    private StorageService storageService;

    @Autowired
    public BlogController(PostRepository postRepository, FileRepository fileRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.fileRepository = fileRepository;
        this.storageService = storageService;
    }

    @GetMapping("/blog")
    public String blogMain(@AuthenticationPrincipal User user,
                           Model model) {
        Iterable<Post> posts = postRepository.findAll();
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
        model.addAttribute("user", user);

        Post post = new Post(title, anons, full_text, user);
        postRepository.save(post);

        Post postWithId = postRepository.getByTitle(title);
        storageService.store(files, postWithId);

        return "redirect:/blog";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable Long id,
                              @AuthenticationPrincipal User user,
                              Model model) {
        /*if (!postRepository.existsById(id))
            return "redirect:/blog";
*/
        Post post = postRepository.findById(id).orElse(null);
        if(post == null)
            return "redirect:/blog";

        List<FileDB> files = storageService.load(id);
        model.addAttribute("files", files);
        model.addAttribute("user", user);
        model.addAttribute("post", post);

        return "blog-details";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable Long id,
                           @AuthenticationPrincipal User user,
                           Model model) {

        Post post = postRepository.findById(id).orElse(null);
        if(post == null)
            return "redirect:/blog";

        List<FileDB> files = storageService.load(id);

        model.addAttribute("files", files);
        model.addAttribute("user", user);
        model.addAttribute("post", post);

        return "blog-edit";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/blog/{id}/edit")
    public String blogPostEdit(@PathVariable Long id,
                           //    @RequestParam MultipartFile file,
                               @RequestParam String title,
                               @RequestParam String anons,
                               @RequestParam String full_text,
                               @RequestParam MultipartFile[] files,
                               @AuthenticationPrincipal User user,
                              Model model) throws IOException {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);

        postRepository.save(post);
        storageService.store(files, post);

        return "redirect:/blog/" + id;
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable long id,
                               Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);

        return "redirect:/blog";
    }
}
