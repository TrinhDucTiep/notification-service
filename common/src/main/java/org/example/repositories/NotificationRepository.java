package org.example.repositories;

import org.example.models.Channel;
import org.example.models.Notification;
import org.example.models.Provider;
import org.example.models.Status;
import org.example.repositories.dto.NotificationAdmin;
import org.example.repositories.dto.NotificationClient;
import org.example.repositories.dto.NotificationDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query(value = """
                    select n.id as notificationId,
                        n.user_id as userId,
                        n.status as status,
                        n.is_read as isRead,
                        n.data as data,
                        n.service_source as serviceSource,
                        n.create_at as createAt,
                        n.end_at as endAt,
                        t.id as templateId,
                        t.channel as channel,
                        t.form as form,
                        t.title as title,
                        s.id as senderid,
                        s.config as config,
                        s.provider as provider
                     from
                    notification n 
                    inner join template t 
                    on n.template_id = t.id
                    inner join sender s 
                    on t.sender_id = s.id
            """, nativeQuery = true)
    List<NotificationDetail> findNotificationDetail(Integer notificationId, Integer templateId, Integer senderId);

    @Query(value = """
        select 
            n.id as id,
            n.user_id as userId,
            n.status as status,
            n.is_read as isRead,
            n.data as data,
            n.rendered_title as renderedTitle,
            n.rendered_content as renderedContent,
            n.service_source as serviceSource,
            n.create_at as createAt,
            n.end_at as endAt,
            n.template_id as templateId,
            t.channel as channel,
            s.id as senderId,
            s.provider as provider
        from notification n
            inner join template t on n.template_id = t.id
            inner join sender s on t.sender_id = s.id
            inner join user u on u.id = n.user_id
        where
            (:userId is null or n.user_id = :userId)
            and
            (:channel is null or t.channel = :channel)
            and
            (:email is null or u.email = :email)
            and
            (:phoneNumber is null or u.phone_number = :phoneNumber)
            and
            (:notificationId is null or n.id = :notificationId)
            and
            (:provider is null or s.provider = :provider)
            and
            (:senderId is null or s.id = :senderId)
            and
            (:status is null or n.status = :status)
            and
            (:createAt is null or n.create_at >= :createAt)
            and
            (:endAt is null or n.end_at >= :endAt)
    """, countQuery = """
        select count(*)
        from notification n
            inner join template t on n.template_id = t.id
            inner join sender s on t.sender_id = s.id
            inner join user u on u.id = n.user_id
        where
            (:userId is null or n.user_id = :userId)
            and
            (:channel is null or t.channel = :channel)
            and
            (:email is null or u.email = :email)
            and
            (:phoneNumber is null or u.phone_number = :phoneNumber)
            and
            (:notificationId is null or n.id = :notificationId)
            and
            (:provider is null or s.provider = :provider)
            and
            (:senderId is null or s.id = :senderId)
            and
            (:status is null or n.status = :status)
            and
            (:createAt is null or n.create_at >= :createAt)
            and
            (:endAt is null or n.end_at >= :endAt)
        """,
            nativeQuery = true)
    Page<NotificationAdmin> getNotificationAdmin(
            @Param("userId") Integer userId,
            @Param("channel") Channel channel,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber,
            @Param("notificationId") Integer notificationId,
            @Param("provider") Provider provider,
            @Param("senderId") Integer senderId,
            @Param("status") Status status,
            @Param("createAt") LocalDateTime createAt,
            @Param("endAt") LocalDateTime endAt,
            Pageable pageable
    );

    @Query(value = """
        select 
            template_id as templateId,
            data as data,
            is_read as isRead,
            end_at as endAt
        from notification
        where
            user_id = :userId
            and
            (:isRead is null or is_read = :isRead)
            and
            (:endAt is null or end_at = :endAt)
    """, countQuery = """
        select count(*)
            from notification
            where
                user_id = :userId
                and
                (:isRead is null or is_read = :isRead)
                and
                (:endAt is null or end_at = :endAt)
""", nativeQuery = true)
    Page<NotificationClient> getNotificationClient(
        @Param("userId") Integer userId,
        @Param("isRead") Boolean isRead,
        @Param("endAt") LocalDateTime endAt,
        Pageable pageable
    );
}
