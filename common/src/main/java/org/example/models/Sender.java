package org.example.models;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "sender")
@AllArgsConstructor
@NoArgsConstructor
public class Sender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "config", columnDefinition = "json")
    private String config;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private Provider provider;

//    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
//    private List<Notification> notifications;

}
