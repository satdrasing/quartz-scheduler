package com.satendra.quartzscheduler.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties("com.satendra.quartz")
@Setter
@Getter
public class QuartzProperties {
    private Resource configLocation;
}
