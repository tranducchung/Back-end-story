package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.Notification;
import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.BlogService;
import com.codegym.service.NotificationService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@CrossOrigin(origins = "*")
public class RestAPIUserController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private NotificationService notificationService;

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

    // share blog to user

    @GetMapping("/api/users/shareToUser/{userId}/blogs/{blogId}")
    public ResponseEntity<Void> shareBlogToUser(@PathVariable("userId") Long userId, @PathVariable("blogId") Long blogId) {

        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userShare = ((UserPrinciple) authen).getUsername();
        Long idUserShare = ((UserPrinciple) authen).getId();
        Blog blog = blogService.findById(blogId);

        System.out.println(userId);
        System.out.println(blogId);

        User userReceive = userService.findUserByID(userId);
        if (userReceive != null && blog != null) {
            Notification notification = new Notification();
            notification.setUserShare(userShare);
            // get user receive by user id

            notification.setUserReceive(userReceive);
            notification.setIdUserShare(idUserShare);
            notification.setIdBlog(blogId);

            // set content notification
            String contentNotification = "/api/users/" + idUserShare + "/blogs/" + blogId;
            notification.setContent(contentNotification);

            notificationService.save(notification);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
}
