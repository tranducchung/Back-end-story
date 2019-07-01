package com.codegym.controller;

import com.codegym.model.User;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
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

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {

        User user = userService.findUserByID(id);
        if ( user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}
