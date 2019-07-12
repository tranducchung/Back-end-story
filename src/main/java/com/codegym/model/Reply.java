package com.codegym.model;

import javax.persistence.*;

@Entity
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

//    @ManyToOne
//    @JoinColumn(name = "comment_id")
//    private Comment repComment;
    @ManyToOne
    @JoinColumn(name = "userRep_id")
    private User userReply;

    public Reply() {
    }

    public Reply(String content) {
        this.content = content;
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

//    public Comment getRepComment() {
//        return repComment;
//    }
//
//    public void setRepComment(Comment repComment) {
//        this.repComment = repComment;
//    }

    public User getUserReply() {
        return userReply;
    }

    public void setUserReply(User userReply) {
        this.userReply = userReply;
    }
}
