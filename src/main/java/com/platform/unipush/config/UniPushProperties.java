package com.platform.unipush.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: platform-base
 * @description:
 * @author: fuyl
 * @create: 2020-05-29 17:24
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "unipush")
public class UniPushProperties {

    private String appId;
    private String appKey;
    private String masterSecret;
    private String url;
}
