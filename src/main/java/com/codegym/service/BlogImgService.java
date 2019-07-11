package com.codegym.service;

import com.codegym.model.BlogImg;
import com.codegym.model.User;

import java.util.List;

public interface BlogImgService {
    void save(BlogImg blogImg);

    List<BlogImg> getAllBlogImgByUser(User user);

    BlogImg findById(Long id);

    BlogImg findByIdAndUser(Long id, User user);
}
