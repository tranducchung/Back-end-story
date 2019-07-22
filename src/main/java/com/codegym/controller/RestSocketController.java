package com.codegym.controller;

import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/socket")
@CrossOrigin("*")
public class RestSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> userRest(@RequestBody Map<String, String> message, HttpServletRequest request) {

        String jwt = request.getHeader("Authorization");
        // get user from token
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userID = ((UserPrinciple) principal).getId();

        User user = userService.findUserByID(userID);

        if (message.containsKey("message")) {
           sendMessage(message);
            return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @MessageMapping("/send/message")
    public Map<String, String> useSocketCommunication(String message) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> messageConverted = null;
        try {
            messageConverted = mapper.readValue(message, Map.class);
        } catch (IOException e) {
            messageConverted = null;
        }
        if (messageConverted != null) {
           sendMessage(messageConverted);
        }
        return messageConverted;
    }

    public void sendMessage(Map<String, String> message) {
        if (message.containsKey("toId") && message.get("toId") != null && !message.get("toId").equals("")) {
            this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.get("toId"), message);
            this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.get("fromId"), message);
        } else {
            this.simpMessagingTemplate.convertAndSend("/socket-publisher", message);
        }
    }
}
