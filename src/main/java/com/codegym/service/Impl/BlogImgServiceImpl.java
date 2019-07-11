package com.codegym.service.Impl;

import com.codegym.model.BlogImg;
import com.codegym.model.User;
import com.codegym.repository.BlogImgRepository;
import com.codegym.service.BlogImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogImgServiceImpl implements BlogImgService {

    @Autowired
    private BlogImgRepository blogImgRepository;

    @Override
    public void save(BlogImg blogImg) {
        blogImgRepository.save(blogImg);
    }

    @Override
    public List<BlogImg> getAllBlogImgByUser(User user) {
        return blogImgRepository.findAllByUser(user);
    }


    @Override
    public BlogImg findById(Long id) {
        return blogImgRepository.findById(id).get();
    }
}
