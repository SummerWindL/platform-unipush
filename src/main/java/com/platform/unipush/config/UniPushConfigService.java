package com.platform.unipush.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class UniPushConfigService implements ApplicationContextAware {

    private final static Log log = LogFactory.getLog(UniPushConfigService.class);

    private static ApplicationContext context;

    private UniPushConfigService() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(final String beanName) {
        if (context == null) {
            return null;
        }
        Object obj = null;
        try {
//			 log.info("load spring bean [" + service + "]");
            obj = context.getBean(beanName);
        } catch (Exception e) {
            log.error("load spring bean [" + beanName + "] failed");
            return null;
        }
        return (T) obj;
    }

    public static String getBeanName(Class<?> clz) {
        if (context == null) {
            return null;
        }
        String beanName = null;
        try {
            beanName = context.getBeanNamesForType(clz)[0];
        } catch (Exception e) {
            return null;
        }
        return beanName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        UniPushConfigService.context = applicationContext;
    }

}
