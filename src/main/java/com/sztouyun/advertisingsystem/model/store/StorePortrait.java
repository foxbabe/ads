package com.sztouyun.advertisingsystem.model.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "index_store_id",columnList = "store_id")
})
public class StorePortrait{
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    /**
     * 门店号码
     */
    @Column(name = "store_id",nullable = false, length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfoExtension storeInfoExtension;

    @Column
    private Integer StorePortraitType;

    @Column
    private Integer value;

    public StorePortrait(String storeId, Integer storePortraitType, Integer value) {
        this.storeId = storeId;
        StorePortraitType = storePortraitType;
        this.value = value;
    }
}
