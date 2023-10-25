package org.example.models;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "history")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "sender_id")
    private Integer senderId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "response", columnDefinition = "json")
    private String response;

    @Column(name = "update_at", columnDefinition = "DATETIME")
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "noti_id")
    private Notification notification;
}
