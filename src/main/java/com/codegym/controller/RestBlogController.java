package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.User;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.BlogService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestBlogController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private BlogService blogService;

    // get all blog

//    @GetMapping("/api/blogs")
//    public ResponseEntity<List<Blog>> getAllBlog() {
//        List<Blog> listBlog = blogService.findAll();
//        if (listBlog.isEmpty()) {
//            return new ResponseEntity<List<Blog>>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
//    }

    // get 1 blog

    @GetMapping("/api/blogs/{id}")
    public ResponseEntity<Blog> getBlog(@PathVariable("id") Long id) {
        Blog blog = blogService.findById(id);
        if (blog == null) {
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Blog>(blog, HttpStatus.OK);
    }

    //get 1 custom blog of custom user

    @RequestMapping(value = {"/api/users/{userId}/blogs/{blogId}"}, method = RequestMethod.GET)
    public ResponseEntity<Blog> getCustomBlog(@PathVariable("userId") Long id, @PathVariable("blogId") Long blogId) {
        User user = userService.findUserByID(id);
        Blog blog = blogService.findByIdAndUser(blogId, user);
        if( user == null) {
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        if( blog == null) {
            return  new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Blog>(blog, HttpStatus.OK);
    }




    // create blog

    @PostMapping("/api/blogs/create")
    public ResponseEntity<Void> createBlog(@RequestBody Blog blog, HttpServletRequest request) {

        String jwt = request.getHeader("Authorization");
        // get user from token
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userID = ((UserPrinciple) principal).getId();

        User user = userService.findUserByID(userID);
        blog.setUser(user);
        // create date
        LocalDateTime localDateTime = LocalDateTime.now();
        Date zonedDateTime = new Date();
        ZoneId.of("Asia/Ho_Chi_Minh");
        System.out.println(zonedDateTime);
        blog.setCreateDate(zonedDateTime);
        blogService.save(blog);
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setLocation(ucBuilder.path("/blog/{id}").buildAndExpand(blog.getId()).toUri());
        return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
    }


    // delete blog

    @RequestMapping(value = {"/api/blogs/{id}"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteBlog(@PathVariable("id") Long id) {
        Blog blog = blogService.findById(id);
        if (blog != null) {
            blogService.remote(blog);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }


    // edit blog

    @RequestMapping(value = {"/api/blogs/{id}"}, method = RequestMethod.PUT)
    public ResponseEntity<Blog> editBlog(@RequestBody Blog blog, @PathVariable("id") Long id) {

        Blog blogInDB = blogService.findById(id);
        if (blogInDB == null) {
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        blogInDB.setTitle(blog.getTitle());
        blogInDB.setContent(blog.getContent());
        blogService.save(blogInDB);
        return new ResponseEntity<Blog>(blogInDB, HttpStatus.OK);
    }

    //get all blog in database by id and DESC


//    @RequestMapping(value = {"/api/blogs-getall"}, method = RequestMethod.GET)
//    public ResponseEntity<List<Blog>> getAllBlogSortedByIdDESC() {
//        List<Blog> listBlog = blogService.findAllBlogByIdOderById();
//        if( listBlog.isEmpty()) {
//            return new ResponseEntity<List<Blog>>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
//    }


    // get alll blog in database by id blog sorted DESC when user_id = ?

    @RequestMapping(value = {"/api/blogs/getall"}, method = RequestMethod.GET)
    public ResponseEntity<List<Blog>> getAllBlogByUserIdAndSortBlogIdDESC() {
        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((UserPrinciple) authen).getId();
        List<Blog> listBlog = blogService.findAllByUserId(userId);
        if (listBlog.isEmpty()) {
            return new ResponseEntity<List<Blog>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
    }


}
