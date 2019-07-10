package com.codegym.service.Impl;

import com.codegym.model.BlogImg;
import com.codegym.repository.BlogImgRepository;
import com.codegym.service.BlogImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogImgServiceImpl implements BlogImgService {

    @Autowired
    private BlogImgRepository blogImgRepository;

    @Override
    public void save(BlogImg blogImg) {
        blogImgRepository.save(blogImg);
    }
}
