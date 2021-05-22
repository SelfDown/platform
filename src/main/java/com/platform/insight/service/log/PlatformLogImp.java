package com.platform.insight.service.log;

import com.alibaba.fastjson.JSON;

import java.util.logging.Logger;

public class PlatformLogImp implements PlatformLog {
    private static Logger logger = Logger.getLogger("platform_log");

    public PlatformLogImp() {

    }

    @Override
    public Object log(String belong, Object o) {
        if (o instanceof String) {
            logger.info("服务号：" + belong+";"+o.toString());
        } else {
            logger.info("服务号：" + belong+";"+JSON.toJSONString(o));
        }

        return true;
    }
}
