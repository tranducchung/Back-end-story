package com.codegym.service;

import com.codegym.model.Blog;
import com.codegym.model.User;
import java.util.List;

public interface BlogService {
    List<Blog> findAll();
    Blog findById(Long id);
    void save(Blog blog);
    void remote(Blog blog);

    List<Blog> findAllBlogByIdOderById();

    List<Blog> findAllByUserId(Long id);

    Blog findByIdAndUser(Long id, User user);

    List<Blog> findAllByTitleContaining(String title);

    List<Blog> findAllByTitleContainingAndUser(String title, User user);
}
