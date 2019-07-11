package com.codegym.service;

import com.codegym.model.BlogImg;

public interface BlogImgService {
    void save(BlogImg blogImg);
   // List<BlogImg> getAllBlogImgByUser(User user);

    BlogImg findById(Long id);
}
