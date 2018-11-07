package com.sztouyun.advertisingsystem.viewmodel.system;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class AuditReasonConfigTreeViewModel {
    private String id;
    private String name;
    private List<AuditReasonConfigTreeViewModel> children;

    public AuditReasonConfigTreeViewModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public AuditReasonConfigTreeViewModel(){}

}
