package org.example.models;

import javax.persistence.*;
import lombok.*;
import org.example.enumerate.Channel;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "template")
@AllArgsConstructor
@NoArgsConstructor
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "channel")
    @Enumerated(EnumType.STRING)
    private Channel channel;

    @Column(name = "form")
    private String form;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToOne
    @JoinColumn(name = "sender_id")
    private Sender sender;
}
