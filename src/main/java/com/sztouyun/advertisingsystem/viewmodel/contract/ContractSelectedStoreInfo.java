package com.sztouyun.advertisingsystem.viewmodel.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractSelectedStoreInfo {
   private String contractId;
   private int storeType;
}
