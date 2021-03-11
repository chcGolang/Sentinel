
package com.alibaba.csp.sentinel.dashboard.rule.nacos.publisher;

import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfig;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.convert.DegradeRuleConvert;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author chc
 */
@Component("degradeRuleNacosPublisher")
public class DegradeRuleNacosPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {
    @Autowired
    private NacosConfig nacosConfig;
    @Autowired
    private SentinelApiClient sentinelApiClient;
    @Autowired
    private AppManagement appManagement;
    @Autowired
    private ConfigService configService;
    @Autowired
    private DegradeRuleConvert converter;

    @Override
    public void publish(String app, List<DegradeRuleEntity> rules) throws Exception {
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
                sentinelApiClient.setDegradeRuleOfMachine(app, machine.getIp(), machine.getPort(), rules);
            }
            return;
        }

        boolean success = configService.publishConfig(app + NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX,
            NacosConfigUtil.GROUP_ID, converter.convert(rules));

        if(!success){
            throw new RuntimeException("publish to nacos fail");
        }
    }
}
