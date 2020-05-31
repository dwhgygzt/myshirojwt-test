package com.example.app.config.shiro;

import com.example.app.config.filter.MyTestFilter;
import org.guzt.starter.shirojwt.filter.ExtraFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.LinkedHashMap;

/**
 * 自定义filter
 * <p>
 * admin
 */
@Component
public class MyExtraFilter extends ExtraFilter {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void setExtraFilter(LinkedHashMap<String, Filter> filterMap) {
        logger.info("MyExtraFilter 实例化...");
        filterMap.put("myTestFilter", new MyTestFilter());
    }
}
