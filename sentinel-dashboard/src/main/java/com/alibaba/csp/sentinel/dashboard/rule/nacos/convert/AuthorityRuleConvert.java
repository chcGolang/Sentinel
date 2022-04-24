package com.alibaba.csp.sentinel.dashboard.rule.nacos.convert;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AbstractRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chc
 */
public class AuthorityRuleConvert implements Converter<List<AuthorityRuleEntity>, String> {

    @Override
    public String convert(List<AuthorityRuleEntity> authorityRuleEntities) {
        if(authorityRuleEntities==null){
            return null;
        }
        List<AuthorityRule> rules = new ArrayList<>();
        authorityRuleEntities.stream().map(AbstractRuleEntity::getRule).forEach(rules::add);
        return JSON.toJSONString(rules,true);
    }
}