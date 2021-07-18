package com.arman.site.models;

import javax.persistence.*;

@Entity
public class FileDB {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String path;
    private String name;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    public FileDB() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public FileDB(String path, String name, Post post) {
        this.path = path;
        this.name = name;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
