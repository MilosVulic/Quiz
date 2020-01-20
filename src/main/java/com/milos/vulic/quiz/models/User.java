package com.milos.vulic.quiz.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;

    private String name;
    private String lastName;
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;

    private String username;
    private String password;

}
