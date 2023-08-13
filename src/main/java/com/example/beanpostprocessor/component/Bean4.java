package com.example.beanpostprocessor.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : KaelvihN
 * @date : 2023/8/12 19:27
 */

@ConfigurationProperties(prefix = "java")
@Data
public class Bean4 {
    private String home;

    private String version;
}
