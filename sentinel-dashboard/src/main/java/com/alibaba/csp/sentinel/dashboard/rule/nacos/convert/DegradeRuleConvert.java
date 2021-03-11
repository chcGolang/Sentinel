package com.alibaba.csp.sentinel.dashboard.rule.nacos.convert;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chc
 */
public class DegradeRuleConvert implements Converter<List<DegradeRuleEntity>, String> {

    @Override
    public String convert(List<DegradeRuleEntity> degradeRuleEntities) {
        if(degradeRuleEntities==null){
            return null;
        }
        List<DegradeRule> rules = new ArrayList<>();
        for (DegradeRuleEntity entity : degradeRuleEntities) {
            DegradeRule rule = new DegradeRule();
            rule.setResource(entity.getResource());
            rule.setLimitApp(entity.getLimitApp());
            if(entity.getCount()!=null){
                rule.setCount(entity.getCount());
            }
            if(entity.getTimeWindow()!=null){
                rule.setTimeWindow(entity.getTimeWindow());
            }
            if(entity.getGrade()!=null){
                rule.setGrade(entity.getGrade());
            }

            rules.add(rule);
        }
        return JSON.toJSONString(rules,true);
    }
}
