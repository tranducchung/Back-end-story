package com.codegym.service;

import com.codegym.model.ConfirmationToken;

public interface ConfirmationTokenService {
    ConfirmationToken findByToken(String token);
}
