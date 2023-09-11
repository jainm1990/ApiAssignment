package com.api.assignment.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

//    @Column(name = "USER_ID", unique = true, nullable = false)
//    private String userId;

    @Column(name = "USER_FIRSTNAME", nullable = false)
    private String firstName;

    @Column(name = "USER_LASTNAME")
    private String lastName;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "MOBILENO", nullable = false)
    private String mobileNumber;

    @OneToMany(targetEntity = UserImages.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "IMAGE_fk", referencedColumnName = "id")
    private List<UserImages> images;

}
