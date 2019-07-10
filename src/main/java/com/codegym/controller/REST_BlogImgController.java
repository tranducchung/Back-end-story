package com.codegym.controller;

import com.codegym.model.BlogImg;
import com.codegym.model.MyUpload;
import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.BlogImgService;
import com.codegym.service.MyUpLoadService;
import com.codegym.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class REST_BlogImgController {
    @Autowired
    private UserService userService;

    @Autowired
    private BlogImgService blogImgService;

    @Autowired
    private MyUpLoadService myUpLoadService;

    @PostMapping(value = "/api/blogimgs")
    public ResponseEntity<Void> createBlogImg(@RequestParam("data") String blogImg, @RequestParam("file") MultipartFile file) {
        if (blogImg == null || file == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        MyUpload myUpload = new MyUpload();
        String fileName = ramdom() + file.getOriginalFilename();
        myUpload.setSrcImg(fileName);
        myUpload.setUser(getUserFromToken());
        myUpLoadService.save(myUpload);
        myUpLoadService.store(file, fileName);
        List<MyUpload> listUpload = new ArrayList<>();
        listUpload.add(myUpload);
        ObjectMapper mapper = new ObjectMapper();
        try {
            BlogImg blogImg2 = mapper.readValue(blogImg, BlogImg.class);
            blogImg2.setListImg(listUpload);
            blogImg2.setUser(getUserFromToken());
            blogImgService.save(blogImg2);
            System.out.println(blogImg2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/blogimgs/create")
    public List < ResponseEntity<Void> > createBlogImgs(@RequestParam("data") String blogImg,@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> createBlogImg(blogImg,file))
                .collect(Collectors.toList());
    }

    private User getUserFromToken() {
        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id_user = ((UserPrinciple)authen).getId();
        User user = userService.findUserByID(id_user);
        return user;
    }

    private static Long ramdom() {
        return (long) Math.floor((Math.random() * 1000000));
    }
}
