package com.sztouyun.advertisingsystem.common.operation.rule.data;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RuleData implements IRuleData {
    private Map<IRuleType,Object> ruleResultInfoMap =new HashMap<>();
}
