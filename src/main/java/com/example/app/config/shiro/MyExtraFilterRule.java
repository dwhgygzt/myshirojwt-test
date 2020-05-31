package com.example.app.config.shiro;

import org.guzt.starter.shirojwt.filter.ExtraFilterRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * 有其他url需要过滤的
 * <p>
 * admin
 */
@Component
public class MyExtraFilterRule extends ExtraFilterRule {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void setExtraFilterRule(LinkedHashMap<String, String> filterRuleMap) {
        logger.info("MyExtraFilterRule 实例化...");
        filterRuleMap.put("/api/init", "noSessionCreation,anon");
        filterRuleMap.put("/api/selectUserInfoByUserName", "noSessionCreation,myTestFilter,jwt,jwtPerms[dd]");
    }
}
