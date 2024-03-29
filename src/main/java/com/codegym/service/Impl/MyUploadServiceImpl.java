package com.codegym.service.Impl;

import com.codegym.model.BlogImg;
import com.codegym.model.MyUpload;
import com.codegym.repository.MyUploadRepository;
import com.codegym.service.MyUpLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class MyUploadServiceImpl implements MyUpLoadService {


//    @Value("/home/chung/Documents/DUAN/Back-end-story/src/main/resources/upload-dir/")
//    private URI path;


    private final Path rootLocation = Paths.get("/home/chung/Documents/DUAN/Back-end-story/src/main/resources/upload-dir/");


    @Autowired
    private MyUploadRepository myUploadRepository;

    @Override
    public void save(MyUpload myUpload) {
        myUploadRepository.save(myUpload);
    }


    @Override
    public void store(MultipartFile file, String fileName) {
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName));
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }

    @Override
    public Resource loadFile(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }

//    @Override
//    public List<MyUpload> findAllUploadFromUserId(Long id) {
//        return myUploadRepository.findAllByUserId(id);
//    }


    @Override
    public MyUpload findMyUploadById(Long id) {
        return myUploadRepository.findById(id).get();
    }

    @Override
    public void deleteUpload(MyUpload myUpload) {
        myUploadRepository.delete(myUpload);
    }

    @Override
    public List<MyUpload> findAllByBlogImg(BlogImg blogImg) {
        return myUploadRepository.findAllByBlogImg(blogImg);
    }

    public List<MyUpload> findByBlogImg(BlogImg blogImg) {
        return myUploadRepository.findAllByBlogImg(blogImg);
    }

    @Override
    public void deleteAllByBlogImg(Long id) {
        myUploadRepository.deleteAllByBlogImgId(id);
    }
}
