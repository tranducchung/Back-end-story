package com.codegym.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "blogImg")
public class BlogImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String createDate;

    @OneToMany(targetEntity = MyUpload.class, mappedBy = "blogImg")
    private List<MyUpload> listImg;

    @ManyToOne()
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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
