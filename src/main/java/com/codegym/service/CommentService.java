package com.codegym.service;

import com.codegym.model.Comment;

public interface CommentService {
    void save(Comment comment);

    Comment findCommentById(Long id);
}
