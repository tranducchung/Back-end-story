package com.codegym.service;

import com.codegym.model.Notification;
import com.codegym.model.User;

import java.util.List;

public interface NotificationService {
    void save(Notification notification);
    List<Notification> findAll();
    void delete(Notification notification);

    List<Notification> findAllByUserReceiveOrderById(User user);

    void deleteAllNotificationByUser(User user);

    // delete notification by id

    void deleteNotificationById(Long id);
}
