package com.api.assignment.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "userImage_details")
public class UserImages {


    @Id
    @Column(name = "IMAGE_ID")
    private long imageid;

//    @ManyToOne
//    @JoinColumn(name="image_id", nullable=false)
//    public UserDetails imageList;

    @Column(name = "IMAGE_LINK")
    private String imageLink;

    @Column(name = "IMAGE_HASH")
    private String hash;


}
