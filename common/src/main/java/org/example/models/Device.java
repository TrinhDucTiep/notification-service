package org.example.models;


import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "device")
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
}
