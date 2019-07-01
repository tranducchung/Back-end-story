package com.codegym.service.Impl;

import com.codegym.model.ConfirmationToken;
import com.codegym.repository.ConfirmationTokenRepository;
import com.codegym.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {


    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public ConfirmationToken findByToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }

    @Override
    public void save(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }
}
