package com.platform.unipush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: platform-unipush
 * @description:
 * @author: fuyl
 * @create: 2020-06-19 16:15
 **/
@EnableAutoConfiguration
@ComponentScan(value = "com.platform")
public class PlatformUnipushApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlatformUnipushApplication.class);
    }
}
