package com.arman.site.service;

import com.arman.site.models.Comment;
import com.arman.site.models.Post;
import com.arman.site.models.User;
import com.arman.site.repository.CommentRepository;
import com.arman.site.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }


    public void addComment(String text, User user, Long post_id) {
        Post post = postRepository.findById(post_id).orElse(null);
        Comment comment = new Comment(text, user, post);

        commentRepository.save(comment);
    }
}
