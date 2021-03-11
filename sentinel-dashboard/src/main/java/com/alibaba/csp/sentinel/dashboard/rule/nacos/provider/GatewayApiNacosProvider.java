package com.alibaba.csp.sentinel.dashboard.rule.nacos.provider;

import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfig;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("gatewayApiNacosProvider")
public class GatewayApiNacosProvider implements DynamicRuleProvider<List<ApiDefinitionEntity>> {
    @Autowired
    private NacosConfig nacosConfig;
    @Autowired
    private SentinelApiClient sentinelApiClient;
    @Autowired
    private AppManagement appManagement;
    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String , List<ApiDefinitionEntity>> converter;
    @Override
    public List<ApiDefinitionEntity> getRules(String appName) throws Exception {

        if(!nacosConfig.isEnable()){
            List<MachineInfo> list = appManagement.getDetailApp(appName).getMachines()
                    .stream()
                    .filter(MachineInfo::isHealthy)
                    .sorted((e1, e2) -> Long.compare(e2.getLastHeartbeat(), e1.getLastHeartbeat())).collect(Collectors.toList());
            if (list.isEmpty()) {
                return new ArrayList<>();
            } else {
                MachineInfo machine = list.get(0);
                return sentinelApiClient.fetchApis(machine.getApp(), machine.getIp(), machine.getPort()).get();
            }
        }

        String rules = configService.getConfig(appName+ NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX
        ,NacosConfigUtil.GROUP_ID,3000);
        if(StringUtil.isEmpty(rules)){
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}