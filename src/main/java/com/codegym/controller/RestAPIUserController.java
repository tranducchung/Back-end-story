package com.codegym.controller;

import com.codegym.model.User;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestAPIUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> listUser = userService.findAllUser();
        if( listUser.isEmpty()) {
            return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<User>>(listUser, HttpStatus.OK);
    }
}
