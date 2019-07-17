package com.codegym.controller;

import com.codegym.config.MyConstants;
import com.codegym.model.Blog;
import com.codegym.model.BlogImg;
import com.codegym.model.Notification;
import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class RestAPIUserController {
    @Autowired
    private BlogImgService blogImgService;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> listUser = userService.findAllUser();
        if (listUser.isEmpty()) {
            return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<User>>(listUser, HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {

        User user = userService.findUserByID(id);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    @GetMapping("/api/users/shareToUser/{userId}/blogs/{blogId}")
    public ResponseEntity<Void> shareBlogToUser(@PathVariable("userId") Long userId,
                                                @PathVariable("blogId") Long blogId) {
        User userShare = getUserFromToken();
        Long idUserShare = userShare.getId();
        Blog blog = blogService.findById(blogId);
        User userReceive = userService.findUserByID(userId);
        if (userReceive != null && blog != null) {
            String content = idUserShare + "/blog/" + blogId;
            Notification notification = new Notification(content, userShare.getUsername(), idUserShare, blogId, userReceive);
            notificationService.save(notification);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    // share blog to user by email

    @GetMapping("/api/users/shareToUser/byEmail/{userId}/blogs/{blogId}")
    public ResponseEntity<Void> shareBlogToUserByEmail(@PathVariable("userId") Long userId,
                                                       @PathVariable("blogId") Long blogId) {
        if (userId != null && blogId != null) {
            User user_share = getUserFromToken();
            Long user_id_share = user_share.getId();
            String content = user_share.getUsername() + " shared to you his blog by Email: " + "http://localhost:4200/notification/" + user_id_share + "/blog/" + blogId;
            User userReceive = userService.findUserByID(userId);
            Blog blogShare = blogService.findById(blogId);
            if (userReceive == null || blogShare == null) {
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }
            Notification notification = new Notification(content, user_share.getUsername(), user_id_share, blogId, userReceive);
            notificationService.save(notification);
            sendEmail(userReceive.getEmail(), MyConstants.MY_EMAIL, "Share Blog", content);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    // share blog img by email

    @GetMapping("/api/users/shareToUser/byEmail/{userId}/blogImg/{blogImgID}")
    public ResponseEntity<Void> shareBlogImgByEmail(@PathVariable("userId") Long userId,
                                                    @PathVariable("blogImgID") Long blogImgID) {
        if (userId != null && blogImgID != null) {
            User userShare = getUserFromToken();
            Long idUserShare = userShare.getId();
            User userReceive = userService.findUserByID(userId);
            BlogImg blogImg = blogImgService.findById(blogImgID);
            String content = userShare.getUsername() + " share to you his album IMG: " + "http://localhost:4200/notification/" + userShare.getId() + "/blogImg/" + blogImgID;
            if (userReceive == null || blogImg == null) {
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }
            Notification notification = new Notification(content, userShare.getUsername(), idUserShare, blogImgID, userReceive);
            notificationService.save(notification);
            sendEmail(userReceive.getEmail(), MyConstants.MY_EMAIL, "Share Blog Img", content);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    // share blog img by system

    @GetMapping(value = "/api/users/shareToUser/{userId}/blogImg/{blogImgID}")
    public ResponseEntity<Void> shareBlogImgBySystem(@PathVariable("userId") Long userId,
                                                     @PathVariable("blogImgID") Long blogImgID) {

        User userShare = getUserFromToken();
        Long idUserShare = userShare.getId();
        User userReceive = userService.findUserByID(userId);
        BlogImg blogImg = blogImgService.findById(blogImgID);
        if (blogImg != null && blogImg != null) {
            String content = idUserShare + "/blogImg/" + blogImgID;
            Notification notification = new Notification(content, userShare.getUsername(), idUserShare, blogImgID, userReceive);
            notificationService.save(notification);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }

        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }


    private void sendEmail(String sendTo, String sendFrom, String title, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setFrom(sendFrom);
        simpleMailMessage.setText(content);
        sendEmailService.sendEmail(simpleMailMessage);
    }

    private User getUserFromToken() {
        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = ((UserPrinciple) authen).getId();
        User user = userService.findUserByID(user_id);
        return user;
    }
}




























