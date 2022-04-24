package com.alibaba.csp.sentinel.dashboard.rule.nacos.convert;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chc
 */
public class SystemRuleConvert implements Converter<List<SystemRuleEntity>, String> {

    @Override
    public String convert(List<SystemRuleEntity> systemRuleEntities) {
        if(systemRuleEntities==null){
            return null;
        }
        List<SystemRule> rules = new ArrayList<>();
        for (SystemRuleEntity entity : systemRuleEntities) {
            SystemRule rule = new SystemRule();
            if(entity.getAvgRt()!=null){
                rule.setAvgRt(entity.getAvgRt());
            }
            if(entity.getHighestCpuUsage()!=null){
                rule.setHighestCpuUsage(entity.getHighestCpuUsage());
            }
            if(entity.getHighestSystemLoad()!=null){
                rule.setHighestSystemLoad(entity.getHighestSystemLoad());
            }
            if(entity.getMaxThread()!=null){
                rule.setMaxThread(entity.getMaxThread());
            }
            if(entity.getQps()!=null){
                rule.setQps(entity.getQps());
            }
            rules.add(rule);
        }
        return JSON.toJSONString(rules,true);
    }
}