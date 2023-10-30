package org.example.models;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enumerate.Status;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "data", columnDefinition = "json")
    private String data;

    @Column(name = "service_source")
    private String serviceSource;

    @Column(name = "create_at", columnDefinition = "DATETIME")
    private LocalDateTime createAt;

    @Column(name = "end_at", columnDefinition = "DATETIME")
    private LocalDateTime endAt;

    @Column(name = "rendered_title")
    private String renderedTitle;

    @Column(name = "rendered_content")
    private String renderedContent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;
}
