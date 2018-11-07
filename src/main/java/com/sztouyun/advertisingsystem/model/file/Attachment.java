package com.sztouyun.advertisingsystem.model.file;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
public class Attachment{
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,length = 36)
    private String objectId;

    @Column( nullable = false)
    private String url;

    @Column(nullable = false,updatable = false)
    private Date createdTime = new Date();
}
