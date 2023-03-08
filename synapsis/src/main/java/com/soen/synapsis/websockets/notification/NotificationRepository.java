package com.soen.synapsis.websockets.notification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.id = :notificationId")
    Notification getNotificationById(@Param("notificationId") Long id);

    @Query("SELECT n FROM Notification n WHERE n.recipient.id = :userId and n.creationTime > :time order by n.creationTime desc")
    List<Notification> getNotificationsByUserIdAfterTime(@Param("userId") Long id, Timestamp time, Pageable pageable);
}
