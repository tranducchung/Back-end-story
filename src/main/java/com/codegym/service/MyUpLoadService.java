package com.codegym.service;

import com.codegym.model.BlogImg;
import com.codegym.model.MyUpload;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MyUpLoadService {
    void save(MyUpload myUpload);
//    void store(MultipartFile file);
    Resource loadFile(String fileName);
    void store(MultipartFile file, String fileName);
    //List<MyUpload> findAllUploadFromUserId(Long id);

    MyUpload findMyUploadById(Long id);
    void deleteUpload(MyUpload myUpload);
    List<MyUpload> findAllByBlogImg(BlogImg blogImg);
    List<MyUpload> findByBlogImg(BlogImg blogImg);

    void deleteAllByBlogImg(Long id);
}
