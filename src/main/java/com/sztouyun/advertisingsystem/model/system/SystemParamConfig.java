package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.common.ITree;
import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Data
@Entity
public class SystemParamConfig extends BaseModel implements ITree<String> {

    public static final String ROOT_PARENT_ID = "0";

    @Column(nullable = false)
    private Integer type;

    @Column(nullable = false)
    private Integer value;

    @Column(name="parent_id",length = 36)
    private String parentId;

    @Column(nullable = false)
    private Boolean enabled;

    @Transient
    private String name;

    public EnumMessage<Integer> getParamValueEnum(){
       return  EnumUtils.toEnum(getValue(),EnumUtils.toEnum(getType(),SystemParamTypeEnum.class).getValueEnum());
    }
}
