package com.codegym.model;

import javax.persistence.*;

@Entity
@Table(name = "myUpload")
public class MyUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String srcImg;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public MyUpload() {
    }

    public MyUpload(String srcImg, User user) {
        this.srcImg = srcImg;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
