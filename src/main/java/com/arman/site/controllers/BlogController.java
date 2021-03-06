package com.arman.site.controllers;

import com.arman.site.models.FileDB;
import com.arman.site.models.Post;
import com.arman.site.repository.FileRepository;
import com.arman.site.repository.PostRepository;
import com.arman.site.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BlogController {
    @Value("${upload.path}")
    private String path;
    private PostRepository postRepository;
    private FileRepository fileRepository;
    private FileStorageService fileStorageService;

    @Autowired
    public BlogController(PostRepository postRepository, FileRepository fileRepository, FileStorageService fileStorageService) {
        this.postRepository = postRepository;
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);

        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam MultipartFile[] files,
                              @RequestParam String title,
                              @RequestParam String anons,
                              @RequestParam String full_text,
                              Model model) throws IOException {
        Post post = new Post(title, anons, full_text);

        postRepository.save(post);
        Post postWithId = postRepository.getByTitle(title);

        fileStorageService.store(files, postWithId);

        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable Long id, Model model) {
        /*if (!postRepository.existsById(id))
            return "redirect:/blog";
*/
        Post post = postRepository.findById(id).orElse(null);
        if(post == null)
            return "redirect:/blog";

      //  Set<Long> filesId = post.getFiles().stream().map(FileDB::getId).collect(Collectors.toSet());

        List<FileDB> files = fileRepository.findAllByPost_id(post.getId());

        model.addAttribute("post", post);
        model.addAttribute("files", files);

        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable Long id, Model model) {

        Post post = postRepository.findById(id).orElse(null);
        if(post == null)
            return "redirect:/blog";

        model.addAttribute("post", post);

        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostEdit(@PathVariable long id,
                               @RequestParam MultipartFile file,
                               @RequestParam String title,
                              @RequestParam String anons,
                              @RequestParam String full_text,
                              Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);

        postRepository.save(post);

        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable long id,
                               Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);

        return "redirect:/blog";
    }
}
