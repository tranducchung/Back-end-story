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

    @ManyToMany
    @JoinTable(
            name = "blogImg_upload",
            joinColumns = @JoinColumn(name = "blogImg_id"),
            inverseJoinColumns = @JoinColumn(name = "upload_id")
    )
    private List<MyUpload> listImg;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public BlogImg() {
    }

    public BlogImg(String title) {
        this.title = title;
    }

    public BlogImg(String title, List<MyUpload> listImg) {
        this.title = title;
        this.listImg = listImg;
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

    public List<MyUpload> getListImg() {
        return listImg;
    }

    public void setListImg(List<MyUpload> listImg) {
        this.listImg = listImg;
    }
}