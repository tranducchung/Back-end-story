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
    @JoinColumn(name = "BlogImg_id")
    private BlogImg blogImg;

    public MyUpload() {
    }

    public MyUpload(String srcImg) {
        this.srcImg = srcImg;
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

    public BlogImg getBlogImg() {
        return blogImg;
    }

    public void setBlogImg(BlogImg blogImg) {
        this.blogImg = blogImg;
    }
}
