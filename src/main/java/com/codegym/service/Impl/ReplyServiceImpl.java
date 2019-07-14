package com.codegym.service.Impl;

import com.codegym.model.Reply;
import com.codegym.repository.ReplyRepository;
import com.codegym.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public void save(Reply reply) {
        replyRepository.save(reply);
    }
}
