package com.codegym.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String title;
    private String urlVideo;
    private String createDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Blog() {
    }

    public Blog(String content, String title, User user) {
        this.content = content;
        this.title = title;
        this.user = user;
    }


//    public Blog(String content, String title, String createDate, User user) {
//        this.content = content;
//        this.title = title;
//        this.createDate = createDate;
//        this.user = user;
//    }
//


    public Blog(String content, String title, String urlVideo, String createDate, User user) {
        this.content = content;
        this.title = title;
        this.urlVideo = urlVideo;
        this.createDate = createDate;
        this.user = user;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}
