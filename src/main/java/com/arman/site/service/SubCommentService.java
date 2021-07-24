package com.arman.site.service;

import com.arman.site.models.Comment;
import com.arman.site.models.SubComment;
import com.arman.site.models.User;
import com.arman.site.repository.CommentRepository;
import com.arman.site.repository.SubCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubCommentService {
    private SubCommentRepository subCommentRepository;
    private CommentRepository commentRepository;
    @Autowired
    public SubCommentService(SubCommentRepository subCommentRepository, CommentRepository commentRepository) {
        this.subCommentRepository = subCommentRepository;
        this.commentRepository = commentRepository;
    }


    public void replyComment(String text, User user, Long comment_id) {
        Comment comment = commentRepository.findById(comment_id).orElse(null);
        SubComment subComment = new SubComment(text, comment, user);

        subCommentRepository.save(subComment);
    }
}
