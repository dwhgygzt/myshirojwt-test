package com.example.app.config.filter;

import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义过滤器
 *
 * admin
 */
public class MyTestFilter extends AuthorizationFilter {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        logger.info("没有别的事情，就是表示进过了过滤器 MyTestFilter");
        return Boolean.TRUE;
    }
}
