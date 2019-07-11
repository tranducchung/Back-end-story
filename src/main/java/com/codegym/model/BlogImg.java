package com.codegym.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "blogImg")
public class BlogImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @OneToMany
    private List<MyUpload> listMyUpload;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public BlogImg() {
    }

    public BlogImg(String title) {
        this.title = title;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MyUpload> getListMyUpload() {
        return listMyUpload;
    }

    public void setListMyUpload(List<MyUpload> listMyUpload) {
        this.listMyUpload = listMyUpload;
    }
}
