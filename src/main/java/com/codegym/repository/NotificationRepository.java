package com.codegym.repository;

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

    @Query("SELECT n FROM Notification n INNER JOIN n.userReceive u WHERE u.id=?1 ORDER BY n.id DESC ")
    List<Notification> findAllNotificationByUserIdAndOderById(Long id);

    void deleteAllByUserReceive(User user);

    List<Notification> findAllByUserReceiveId(Long id);

}
