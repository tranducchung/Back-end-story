package com.codegym.controller;

import com.codegym.model.BlogImg;
import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.BlogImgService;
import com.codegym.service.MyUpLoadService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class REST_BlogImgController {
    @Autowired
    private UserService userService;

    @Autowired
    private BlogImgService blogImgService;

    @Autowired
    private MyUpLoadService myUpLoadService;

    @PostMapping(value = "/api/blogImgs/create")
    public ResponseEntity<Long> createAlbumImgs(@RequestBody BlogImg blogImg,
                                                UriComponentsBuilder uriComponentsBuilder) {
        if (blogImg == null) {
            return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
        }
        blogImg.setUser(getUserFromToken());
        blogImgService.save(blogImg);
        return new ResponseEntity<Long>(blogImg.getId(), HttpStatus.OK);
    }


    private User getUserFromToken() {
        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id_user = ((UserPrinciple) authen).getId();
        User user = userService.findUserByID(id_user);
        return user;
    }

    private static Long ramdom() {
        return (long) Math.floor((Math.random() * 1000000));
    }
}
