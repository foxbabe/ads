package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(name="index_contract_id_store_id", columnList = "contract_id,store_id")
})
@Data
public class ContractStore {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    /**
     * 门店号码
     */
    @Column(name = "store_id",nullable = false, length = 36)
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable = false,updatable = false)
    private StoreInfo storeInfo;


    /**
     * 对应合同id
     */
    @Column(name = "contract_id",length = 36)
    private String contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id",insertable = false,updatable = false)
    private Contract contract;


    /**
     * 门店类型
     */
    @Column(nullable = false)
    private Integer storeType;
}
