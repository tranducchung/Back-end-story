package com.codegym.controller;

import com.codegym.model.Comment;
import com.codegym.model.Reply;
import com.codegym.model.User;
import com.codegym.security.service.UserPrinciple;
import com.codegym.service.CommentService;
import com.codegym.service.ReplyService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class REST_ReplyController {
    @Autowired
    private ReplyService replyService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/api/reps/comment/{idComment}")
    public ResponseEntity<Void> createReplyComment(@RequestBody Reply reply,
                                                   @PathVariable("idComment") Long idComment) {
        List<Reply> replyList = new ArrayList<>();
        Comment comment = commentService.findCommentById(idComment);
        if (comment != null) {
            reply.setUserReply(getUserFromToken());
            //reply.setRepComment(comment);
            replyList.add(reply);
            comment.setReplyList(replyList);
            replyService.save(reply);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    private User getUserFromToken() {
        Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long idUser = ((UserPrinciple)authen).getId();
        User user = userService.findUserByID(idUser);
        return user;
    }

}
