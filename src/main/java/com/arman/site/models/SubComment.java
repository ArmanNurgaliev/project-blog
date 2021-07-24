package com.arman.site.models;

import javax.persistence.*;

@Entity
public class SubComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SubComment(String text, Comment parent, User user) {
        this.text = text;
        this.parent = parent;
        this.user = user;
    }

    public SubComment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Comment getComment() {
        return parent;
    }

    public void setComment(Comment parent) {
        this.parent = parent;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
