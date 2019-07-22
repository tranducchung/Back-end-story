package com.codegym.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "myUpload")
public class MyUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String srcImg;

    @ManyToOne
    @JoinColumn(name = "myUpload_id", nullable = false)
    private BlogImg blogImg;

    public MyUpload() {
    }

    public BlogImg getBlogImg() {
        return blogImg;
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

//    public BlogImg getBlogImg() {
//        return blogImg;
//    }

    public void setBlogImg(BlogImg blogImg) {
        this.blogImg = blogImg;
    }
}
