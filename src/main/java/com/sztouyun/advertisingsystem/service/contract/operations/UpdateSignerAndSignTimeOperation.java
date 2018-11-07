package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UpdateSignerAndSignTimeOperation implements IActionOperation<Contract> {
    @Autowired
    private ContractRepository contractRepository;

    @Override
    public void operateAction(Contract contract) {
        contract.setSignTime(new Date());
        contract.setSignerId(AuthenticationService.getUser().getId());
        contractRepository.save(contract);
    }
}
