package org.example.models;

import javax.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "full_name")
    private String fullName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    List<Device> devices;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications;

}
