package com.sztouyun.advertisingsystem.model.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class VersionInfoReceiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, length = 36)
    private String versionInfoId;

    @NonNull
    @Column(nullable = false, length = 36)
    private String receiverId;

    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    private Boolean hasRead = false;
}
