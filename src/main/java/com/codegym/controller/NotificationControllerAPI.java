package com.codegym.controller;

import com.codegym.model.Notification;
import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.NotificationService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class NotificationControllerAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/api/notifi/user")
    public ResponseEntity<List<Notification>> getListNotification() {
        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((UserPrinciple) authen).getId();
        List<Notification> listNotification = notificationService.findAllByUserReceive(userId);
        if (listNotification.isEmpty()) {
            return new ResponseEntity<List<Notification>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Notification>>(listNotification, HttpStatus.OK);
    }


    @DeleteMapping("/api/allnotifi/user/{id}")
    public ResponseEntity<Void> deleteNotificationByUserId(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.findUserByID(id);
        notificationService.deleteAllNotificationByUser(user);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/api/oneNotifi/{id}")
    public ResponseEntity<Void> deleteOneNotificationById(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        notificationService.deleteNotificationById(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
