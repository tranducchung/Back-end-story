package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.User;
import com.codegym.payload.request.LoginForm;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.BlogService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/api/blogs")
    public ResponseEntity<List<Blog>> getAllBlog() {
        List<Blog> listBlog = blogService.findAll();
        if (listBlog.isEmpty()) {
            return new ResponseEntity<List<Blog>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Blog>>(listBlog, HttpStatus.OK);
    }

    // get 1 blog

    @GetMapping("/api/blogs/{id}")
    public ResponseEntity<Blog> getBlog(@PathVariable("id") Long id) {
        Blog blog = blogService.findById(id);
        if (blog == null) {
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Blog>(blog, HttpStatus.OK);
    }

    // create blog

    @PostMapping("/api/blogs")
    public ResponseEntity<Void> createBlog(@RequestBody Blog blog, UriComponentsBuilder ucBuilder,
                                           HttpServletRequest request,
                                           Authentication authentication) {

        String jwt = request.getHeader("Authorization");
        // get user from token
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userID = ((UserPrinciple)principal).getId();

        User user = userService.findUserByID(userID);
        blog.setUser(user);
        blogService.save(blog);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ucBuilder.path("/blog/{id}").buildAndExpand(blog.getId()).toUri());
        return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
    }


    // delete blog

    @RequestMapping(value = {"/api/blogs/{id}"}, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteBlog(@PathVariable("id") Long id) {
        Blog blog = blogService.findById(id);
        if ( blog != null ) {
            blogService.remote(blog);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }


    // edit blog

    @RequestMapping(value = {"/api/blogs/{id}"}, method = RequestMethod.PUT)
    public ResponseEntity<Blog> editBlog(@RequestBody Blog blog, @PathVariable("id") Long id) {

        Blog blogInDB = blogService.findById(id);
        if( blogInDB == null) {
            return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        blogInDB.setTitle(blog.getTitle());
        blogInDB.setContent(blog.getContent());
        blogService.save(blogInDB);
        return new ResponseEntity<Blog>(blogInDB, HttpStatus.OK);
    }
}
