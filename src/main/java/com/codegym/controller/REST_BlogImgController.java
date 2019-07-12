package com.codegym.controller;

import com.codegym.model.BlogImg;
import com.codegym.model.MyUpload;
import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.BlogImgService;
import com.codegym.service.MyUpLoadService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.util.List;


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

        return new ResponseEntity<Long>(blogImg.getId(),HttpStatus.OK);
    }

    @GetMapping("/api/blogImgs")
    public ResponseEntity<List<BlogImg>> getAllBlogImgByUser() {
        List<BlogImg> blogImgList = blogImgService.getAllBlogImgByUser(getUserFromToken());
        if (blogImgList.isEmpty()) {
            return new ResponseEntity<List<BlogImg>>(blogImgList, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<BlogImg>>(blogImgList, HttpStatus.OK);
    }


    @GetMapping("/api/blogImgs/{id}")
    public ResponseEntity<BlogImg> getBlogImgById(@PathVariable("id") Long id) {
        BlogImg blogImg = blogImgService.findById(id);
        if (blogImg == null) {
            return new ResponseEntity<BlogImg>(blogImg, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<BlogImg>(blogImg, HttpStatus.OK);
    }

    //get custom album img

    @GetMapping("/api/blogImgs/{idBlog}/user/{userId}")
    public ResponseEntity<BlogImg> getCustomAlbumByCustomUserID(@PathVariable("idBlog") Long idBlog,
                                                                @PathVariable("userId") Long userId) {
        if (idBlog != null || userId != null) {
            User user = userService.findUserByID(userId);
            BlogImg blogImg = blogImgService.findByIdAndUser(idBlog, user);
            if ( blogImg == null) {
                return new ResponseEntity<BlogImg>(blogImg, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<BlogImg>(blogImg, HttpStatus.OK);
        }
        return new ResponseEntity<BlogImg>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/api/blogImgs/{idblog}")
    public ResponseEntity<Void> deleteAlbumImg(@PathVariable("idblog") Long idBlog) {
        BlogImg blogImg = blogImgService.findById(idBlog);
        if ( blogImg != null) {
            List<MyUpload> myUploadList = myUpLoadService.findByBlogImg(blogImg);
            deleteImg(myUploadList);
            myUpLoadService.deleteAllByBlogImg(idBlog);
            blogImgService.deleteBlogImg(idBlog);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    private static void deleteImg(List<MyUpload> uploadList) {
        for(int i = 0; i< uploadList.size(); i++) {
            String src = "/home/nbthanh/Du-An/Back-end-story/src/main/resources/upload-dir/" + uploadList.get(i).getSrcImg();
            File file = new File(src);
            if (file.exists()) {
                file.delete();
            }
        }
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
