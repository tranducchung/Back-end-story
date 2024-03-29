package com.codegym.repository;

import com.codegym.model.Blog;
import com.codegym.model.Notification;
import com.codegym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserReceiveOrderByIdDesc(User user);

    void deleteAllByUserReceive(User user);

}
