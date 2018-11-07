package com.sztouyun.advertisingsystem.viewmodel.customer.CustomerVisit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@ApiModel
public class UpdateCustomerVisitViewModel extends CreateCustomerVisitViewModel{

    @ApiModelProperty(value = "客户拜访ID", required = true)
    @NotBlank(message = "客户拜访ID不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
