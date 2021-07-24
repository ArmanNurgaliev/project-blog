package com.arman.site.service;

import com.arman.site.models.Post;
import com.arman.site.models.User;
import com.arman.site.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public void addPost(String title, String anons, String full_text, User user) {
        Post post = new Post(title, anons, full_text, user);
        postRepository.save(post);
    }

    public void editPost(Long post_id, String title, String anons, String full_text) {
        Post post = postRepository.findById(post_id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);

        postRepository.save(post);
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
}
