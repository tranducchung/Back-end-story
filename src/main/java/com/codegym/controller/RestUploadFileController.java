package com.codegym.controller;

import com.codegym.model.Blog;
import com.codegym.model.BlogImg;
import com.codegym.model.MyUpload;
import com.codegym.service.BlogImgService;
import com.codegym.service.MyUpLoadService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestUploadFileController {
    @Autowired
    private BlogImgService blogImgService;

    @Autowired
    private UserService userService;

    @Autowired
    private MyUpLoadService myUpLoadService;

    @PostMapping(value = "/api/upload/{id}")
    public ResponseEntity<Void> upLoadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") Long idBlogImg) {
        if (file == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        try {
            MyUpload myUpload = new MyUpload();
            String fileName = ramdom() + file.getOriginalFilename() ;
            myUpload.setSrcImg(fileName);
            BlogImg blogImg = blogImgService.findById(idBlogImg);
            myUpload.setBlogImg(blogImg);
            myUpLoadService.save(myUpload);
            myUpLoadService.store(file, fileName);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("error = " + e);
            return new ResponseEntity<Void>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/api/upload/multi/{id}")
    public List < ResponseEntity<Void> > uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @PathVariable("id") Long id) {
        return Arrays.asList(files)
                .stream()
                .map(file -> upLoadFile(file, id))
                .collect(Collectors.toList());
    }


    @DeleteMapping("/api/upload/{id}")
    public ResponseEntity<Void> deleteImg(@PathVariable("id") Long id) {
        String url = "/home/nbthanh/Du-An/Back-end-story/src/main/resources/upload-dir/";
        MyUpload myUpload = myUpLoadService.findMyUploadById(id);
        File file = new File(url + myUpload.getSrcImg());
        if (file.exists()) {
            file.delete();
            myUpLoadService.deleteUpload(myUpload);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
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
//
//    @RequestMapping(value = {"/api/upload/getall"}, method = RequestMethod.GET)
//    public ResponseEntity<List<MyUpload>> getAllUploadFromUserId() {
//        // get User id From token
//        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = ((UserPrinciple) authen).getId();
//
//        List<MyUpload> listMyUpload = myUpLoadService.findAllUploadFromUserId(userId);
//        if (listMyUpload.isEmpty()) {
//            return new ResponseEntity<List<MyUpload>>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<List<MyUpload>>(listMyUpload, HttpStatus.OK);
//    }

    @RequestMapping(value = {"/api/upload/multi/AllByBlogImg/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<List<MyUpload>> getAllUploadFromBlogImg(@PathVariable("id") Long id) {
        BlogImg blogImg = blogImgService.findById(id);
        List<MyUpload> listMyUpload = myUpLoadService.findAllByBlogImg(blogImg);
        if (listMyUpload.isEmpty()) {
            return new ResponseEntity<List<MyUpload>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<MyUpload>>(listMyUpload, HttpStatus.OK);
    }

    private static Long ramdom() {
        return (long) Math.floor((Math.random() * 1000000));
    }
}

