package com.codegym.controller;

import com.codegym.model.MyUpload;
import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.MyUpLoadService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestUploadFileController {

    @Autowired
    private UserService userService;

    @Autowired
    private MyUpLoadService myUpLoadService;

    @PostMapping(value = "/api/upload")
    public ResponseEntity<Void> upLoadFile(@RequestBody @RequestParam("file") MultipartFile file) {
        if (file == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        try {
            MyUpload myUpload = new MyUpload();
            // get user from token
            Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = ((UserPrinciple) authen).getId();
            User user = userService.findUserByID(userId);
            myUpload.setSrcImg(file.getOriginalFilename());
            myUpload.setUser(user);
            myUpLoadService.save(myUpload);
            myUpLoadService.store(file);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("error = " + e);
            return new ResponseEntity<Void>(HttpStatus.EXPECTATION_FAILED);
        }
    }


    //get file by file name

//    @RequestMapping(value = {"/api/files/{filename:.+}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//        Resource file = myUpLoadService.loadFile(filename);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,            "attachment; filename=\"" + file.getFilename() + "\"")
//                .body(file);
//    }


    // get all file upload by user id

    @RequestMapping(value = {"/api/upload/getall"}, method = RequestMethod.GET)
    public ResponseEntity<List<MyUpload>> getAllUploadFromUserId() {
        // get User id From token
        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((UserPrinciple) authen).getId();

        List<MyUpload> listMyUpload = myUpLoadService.findAllUploadFromUserId(userId);
        if (listMyUpload.isEmpty()) {
            return new ResponseEntity<List<MyUpload>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<MyUpload>>(listMyUpload, HttpStatus.OK);
    }

}

