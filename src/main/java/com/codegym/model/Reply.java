package com.codegym.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "reply")
public class Reply {
    private Long id;
    private String content;

    private Comment comment;
    private User user;
}
