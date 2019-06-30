package com.codegym.service;

import com.codegym.model.Notification;

import java.util.List;

public interface NotificationService {
    void save(Notification notification);
    List<Notification> findAll();
    void delete(Notification notification);
}
