package com.codegym.model;

import javax.persistence.*;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String userShare;
    @ManyToOne
    @JoinColumn(name = "id_userReceive")
    private User userReceive;

    public Notification() {
    }

    public Notification(String content, String userShare, User userReceive) {
        this.content = content;
        this.userShare = userShare;
        this.userReceive = userReceive;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserShare() {
        return userShare;
    }

    public void setUserShare(String userShare) {
        this.userShare = userShare;
    }

    public User getUserReceive() {
        return userReceive;
    }

    public void setUserReceive(User userReceive) {
        this.userReceive = userReceive;
    }
}
