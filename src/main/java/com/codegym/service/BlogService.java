package com.codegym.service;

import com.codegym.model.Blog;
import com.codegym.model.User;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface BlogService {
    List<Blog> findAll();
    Blog findById(Long id);
    void save(Blog blog);
    void remote(Blog blog);

    List<Blog> findAllBlogByIdOderById();


    List<Blog> findAllByUserId(Long id);

    Blog findByIdAndUser(Long id, User user);

    Optional<Blog> findAllByTitleContaining(String title);
}
