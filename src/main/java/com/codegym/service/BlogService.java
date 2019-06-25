package com.codegym.service;

import com.codegym.model.Blog;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface BlogService {
    List<Blog> findAll();
    Blog findById(Long id);
    void save(Blog blog);
    void remote(Blog blog);

    List<Blog> findAllBlogByIdOderById();


    List<Blog> findAllByUserId(Long id);
}
