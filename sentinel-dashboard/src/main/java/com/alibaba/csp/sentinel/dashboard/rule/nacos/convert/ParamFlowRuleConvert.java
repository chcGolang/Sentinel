package com.alibaba.csp.sentinel.dashboard.rule.nacos.convert;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AbstractRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chc
 */
public class ParamFlowRuleConvert implements Converter<List<ParamFlowRuleEntity>, String> {

    @Override
    public String convert(List<ParamFlowRuleEntity> paramFlowRuleEntities) {
        if(paramFlowRuleEntities==null){
            return null;
        }
        List<ParamFlowRule> rules = new ArrayList<>();
        paramFlowRuleEntities.stream().map(AbstractRuleEntity::getRule).forEach(rules::add);
        return JSON.toJSONString(rules,true);
    }
}
