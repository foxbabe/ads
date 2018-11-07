package com.sztouyun.advertisingsystem.service.common.operations;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import lombok.experimental.var;
import org.springframework.stereotype.Service;

@Service
public class ValidateAuditingPermissionOperation implements IActionOperation<Object> {
    @Override
    public void operateAction(Object v) {
        var user = AuthenticationService.getUser();
        if (user.isAdmin())
            return;

        RoleTypeEnum roleTypeEnum = user.getRoleTypeEnum();
        if (!roleTypeEnum.equals(RoleTypeEnum.ManagerialStaff))
            throw new BusinessException("权限不足！", Constant.NO_PERMISSION);
        return;
    }
}
