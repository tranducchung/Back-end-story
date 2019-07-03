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
    private String iframe;
    private ZonedDateTime createDate;

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

    public Blog(String content, String title, ZonedDateTime createDate, User user) {
        this.content = content;
        this.title = title;
        this.createDate = createDate;
        this.user = user;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
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

    public String getIframe() {
        return iframe;
    }

    public void setIframe(String iframe) {
        this.iframe = iframe;
    }
}
