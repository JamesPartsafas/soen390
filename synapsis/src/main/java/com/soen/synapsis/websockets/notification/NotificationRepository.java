package com.soen.synapsis.websockets.notification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Repository layer for interfacing with Notification database table.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Retrieves a notification by its ID.
     * @param id the ID of the notification to retrieve
     * @return the Notification object if found, null otherwise
     */
    @Query("SELECT n FROM Notification n WHERE n.id = :notificationId")
    Notification getNotificationById(@Param("notificationId") Long id);

    /**
     * Retrieves a list of notifications for a given user after a certain time.
     * The list is sorted in descending order by the creation time.
     * @param id the ID of the user to retrieve notifications for
     * @param time the timestamp to start retrieving notifications from
     * @param pageable pagination information
     * @return the list of Notification objects that meet the criteria
     */
    @Query("SELECT n FROM Notification n WHERE n.recipient.id = :userId and n.creationTime > :time order by n.creationTime desc")
    List<Notification> getNotificationsByUserIdAfterTime(@Param("userId") Long id, Timestamp time, Pageable pageable);
}
