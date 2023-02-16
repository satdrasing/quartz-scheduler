package com.satendra.quartzscheduler.service;


import com.satendra.quartzscheduler.model.JobDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

import static org.quartz.JobKey.jobKey;

@Slf4j
@Service
@Transactional
public class TextMessageService extends AbstractJobService{

    public TextMessageService(Scheduler scheduler) {
        super(scheduler);
    }


    @Override
    public JobDescriptor createJob(String group, JobDescriptor descriptor) {
        descriptor.setGroup(group);
        JobDetail jobDetail = descriptor.buildJobDetail();
        Set<Trigger> triggersForJob = descriptor.buildTriggers();
        log.info("About to save job with key - {}", jobDetail.getKey());
        try {
            scheduler.scheduleJob(jobDetail, triggersForJob, false);
            log.info("Job with key - {} saved sucessfully", jobDetail.getKey());
        } catch (SchedulerException e) {
            log.error("Could not save job with key - {} due to error - {}", jobDetail.getKey(), e.getLocalizedMessage());
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
        return descriptor;
    }


    @Override
    public void updateJob(String group, String name, JobDescriptor descriptor) {
        try {
            JobDetail oldJobDetail = scheduler.getJobDetail(jobKey(name, group));
            if (Objects.nonNull(oldJobDetail)) {
                JobDataMap jobDataMap = oldJobDetail.getJobDataMap();
                jobDataMap.put("messageBody", descriptor.getMessageBody());

                JobBuilder jb = oldJobDetail.getJobBuilder();
                JobDetail newJobDetail = jb.usingJobData(jobDataMap).storeDurably().build();
                scheduler.addJob(newJobDetail, true);
                log.info("Updated job with key - {}", newJobDetail.getKey());
                return;
            }
            log.warn("Could not find job with key - {}.{} to update", group, name);
        } catch (SchedulerException e) {
            log.error("Could not find job with key - {}.{} to update due to error - {}", group, name, e.getLocalizedMessage());
        }
    }

}
