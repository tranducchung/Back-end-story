package com.codegym.service;

import com.codegym.model.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> findAll();
    Blog findById(Long id);
    void save(Blog blog);
    void remote(Blog blog);
}
