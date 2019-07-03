package com.codegym.service.Impl;

import com.codegym.model.Notification;
import com.codegym.model.User;
import com.codegym.repository.NotificationRepository;
import com.codegym.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public void delete(Notification notification) {
        notificationRepository.delete(notification);
    }

    @Override
    public List<Notification> findAllNotificationByUserIdAndOderById(Long id) {
        return notificationRepository.findAllNotificationByUserIdAndOderById(id);
    }

    @Override
    public void deleteAllNotificationByUser(User user) {
        notificationRepository.deleteAllByUserReceive(user);
    }

    @Override
    public void deleteNotificationById(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public List<Notification> findAllByUserReceive(Long id) {
        return notificationRepository.findAllByUserReceiveId(id);
    }
}
