package com.satendra.quartzscheduler.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.util.Assert;

public class AutowireCapableBeanJobFactory extends SpringBeanJobFactory {
    private final AutowireCapableBeanFactory beanFactory;

    public AutowireCapableBeanJobFactory(AutowireCapableBeanFactory beanFactory) {
        Assert.notNull(beanFactory, "Bean factory must not be null");
        this.beanFactory = beanFactory;
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        this.beanFactory.autowireBean(jobInstance);
        //this.beanFactory.initializeBean(jobInstance, null);
        return jobInstance;
    }
}