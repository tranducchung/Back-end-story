package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.Comment;
import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.BlogService;
import com.codegym.service.CommentService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class REST_CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/comments/blogs/{idBlog}")
    public ResponseEntity<Void> createComment(@PathVariable("idBlog") Long idBlog,
                                              @RequestBody Comment comment) {
        List<Comment> commentList = new ArrayList<>();
        Blog blog = blogService.findById(idBlog);
        if( blog == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        comment.setBlog(blog);
        comment.setUser(getUserFromToken());
        commentList.add(comment);
        blog.setCommentList(commentList);
        commentService.save(comment);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }


    private User getUserFromToken() {
        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long idUser = ((UserPrinciple)authen).getId();
        User user = userService.findUserByID(idUser);
        return user;
    }


}
