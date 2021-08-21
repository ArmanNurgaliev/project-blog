package com.arman.site.service;

import com.arman.site.models.Post;
import com.arman.site.models.User;
import com.arman.site.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PostService {
    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Map<String, String> validatePost(String title, String anons, String full_text) {
        Map<String, String> map = new HashMap<>();
        if (title.isEmpty())
            map.put("titleError", "Title can't e empty");
        if (anons.isEmpty())
            map.put("anonsError", "Anons can't e empty");
        if (full_text.isEmpty())
            map.put("textError", "Text can't e empty");
        return map;
    }

    public void addPost(String title, String anons, String full_text, User user) {
        Post post = new Post(title, anons, full_text, user);

        postRepository.save(post);
    }

    public void editPost(Long post_id, String title, String anons, String full_text) {
        Post postFromDB = postRepository.findById(post_id).orElseThrow();
        postFromDB.setTitle(title);
        postFromDB.setAnons(anons);
        postFromDB.setFull_text(full_text);

        postRepository.save(postFromDB);
    }

    public void delete(long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow();
        postRepository.delete(post);
    }

    public Iterable<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long post_id) {
       return postRepository.findById(post_id).orElse(null);
    }

    public Iterable<Post> findAllByTitle(String filter) {
        return postRepository.findByTitleContainingIgnoreCase(filter);
    }
}
