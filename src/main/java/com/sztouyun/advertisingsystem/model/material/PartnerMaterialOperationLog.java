package com.sztouyun.advertisingsystem.model.material;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.*;

@Entity
public class PartnerMaterialOperationLog extends BaseModel {
    /**
     * 对应合作方素材id
     */
    @Column(name = "partner_material_id",nullable = false, length = 36)
    private String partnerMaterialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_material_id",insertable = false,updatable = false)
    private PartnerMaterial partnerMaterial;

    /**
     * 操作
     */
    @Column(nullable = false)
    private Integer operation;

    /**
     * 是否成功
     */
    @Column(nullable = false)
    private boolean successed;

    @Column(name = "reason_id",updatable = false,length = 36)
    private String reasonId;

    @Column(name = "sub_reason_id",updatable = false,length = 36)
    private String subReasonId;

    /**
     * 备注
     */
    @Column(length = 2000)
    private String remark;

    public String getPartnerMaterialId() {
        return partnerMaterialId;
    }

    public void setPartnerMaterialId(String partnerMaterialId) {
        this.partnerMaterialId = partnerMaterialId;
    }

    public PartnerMaterial getPartnerMaterial() {
        return partnerMaterial;
    }

    public void setPartnerMaterial(PartnerMaterial partnerMaterial) {
        this.partnerMaterial = partnerMaterial;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public boolean isSuccessed() {
        return successed;
    }

    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getSubReasonId() {
        return subReasonId;
    }

    public void setSubReasonId(String subReasonId) {
        this.subReasonId = subReasonId;
    }

    public PartnerMaterialOperationLog(){

    }

    public PartnerMaterialOperationLog(String partnerMaterialId, PartnerMaterialOperationEnum operation, boolean successed, String remark) {
        this.partnerMaterialId = partnerMaterialId;
        this.operation = operation.getValue();
        this.successed = successed;
        this.remark = remark;
    }
}
