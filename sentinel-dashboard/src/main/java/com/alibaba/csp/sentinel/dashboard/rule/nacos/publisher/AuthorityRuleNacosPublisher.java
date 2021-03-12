package com.alibaba.csp.sentinel.dashboard.rule.nacos.publisher;

import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfig;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.convert.AuthorityRuleConvert;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author chc
 */
@Component("authorityRuleNacosPublisher")
public class AuthorityRuleNacosPublisher implements DynamicRulePublisher<List<AuthorityRuleEntity>> {

    @Autowired
    private NacosConfig nacosConfig;
    @Autowired
    private SentinelApiClient sentinelApiClient;
    @Autowired
    private AppManagement appManagement;
    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<AuthorityRuleEntity>, String> converter;

    @Override
    public void publish(String app, List<AuthorityRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        if (!nacosConfig.isEnable()){
            Set<MachineInfo> set = appManagement.getDetailApp(app).getMachines();
            for (MachineInfo machine : set) {
                if (!machine.isHealthy()) {
                    continue;
                }
                sentinelApiClient.setAuthorityRuleOfMachine(app, machine.getIp(), machine.getPort(), rules);
            }
            return;
        }

        boolean success = configService.publishConfig(app + NacosConfigUtil.AUTHORITY_DATA_ID_POSTFIX,
            NacosConfigUtil.GROUP_ID, converter.convert(rules));
        if(!success){
            throw new RuntimeException("publish to nacos fail");
        }
    }
}
