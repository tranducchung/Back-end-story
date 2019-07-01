package com.codegym.service.Impl;

import com.codegym.model.MyUpload;
import com.codegym.repository.MyUploadRepository;
import com.codegym.service.MyUpLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MyUploadServiceImpl implements MyUpLoadService {

    //Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get("/home/laptop88/Desktop/Back-end-story/src/main/resources/upload-dir/");

    @Autowired
    private MyUploadRepository myUploadRepository;

    @Override
    public void save(MyUpload myUpload) {
        myUploadRepository.save(myUpload);
    }

    @Override
    public void store(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
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

    @Override
    public List<MyUpload> findAllUploadFromUserId(Long id) {
        return myUploadRepository.findAllByUserId(id);
    }
}
