package com.codegym.controller;

import com.codegym.config.MyConstants;
import com.codegym.model.Blog;
import com.codegym.model.Notification;
import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.BlogService;
import com.codegym.service.NotificationService;
import com.codegym.service.SendEmailService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    public ResponseEntity<Void> shareBlogToUser(@PathVariable("userId") Long userId, @PathVariable("blogId") Long blogId) {

        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userShare = ((UserPrinciple) authen).getUsername();
        Long idUserShare = ((UserPrinciple) authen).getId();
        System.out.println("Id_userShare = " + idUserShare);
        Blog blog = blogService.findById(blogId);

        System.out.println(userId);
        System.out.println(blogId);

        User userReceive = userService.findUserByID(userId);
        if (userReceive != null && blog != null) {
            Notification notification = new Notification();
            notification.setUserShare(userShare);
            // get user receive by user id

            notification.setUserReceive(userReceive);

            // set content notification
            String contentNotification = "/api/users/" + idUserShare + "/blogs/" + blogId;
            notification.setContent(contentNotification);
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
            // get user_share from token
            Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long user_id_share = ((UserPrinciple)authen).getId();
            User user_share = userService.findUserByID(user_id_share);
            String content = user_share.getUsername() + " shared to you his blog by Email: " + "http://localhost:8080/api/users/" + user_id_share + "/blogs/" + blogId;

            // get user_receive by user_id
            User userReceive = userService.findUserByID(userId);
            // get blog share by blogId
            Blog blogShare = blogService.findById(blogId);

            if (userReceive == null || blogShare == null) {
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }
            Notification notification = new Notification();
            notification.setUserReceive(userReceive);
            notification.setUserShare(user_share.getUsername());
            notification.setContent(content);
            notificationService.save(notification);
            // send email
            sendEmail(userReceive.getEmail(), MyConstants.MY_EMAIL, "Share Blog", content);
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
}




























