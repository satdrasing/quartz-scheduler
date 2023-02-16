package com.satendra.quartzscheduler.service;

import com.satendra.quartzscheduler.model.JobDescriptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static org.quartz.JobKey.jobKey;


@Slf4j
@RequiredArgsConstructor
public abstract class AbstractJobService implements JobService {

    protected final Scheduler scheduler;

    @Override
    public abstract JobDescriptor createJob(String group, JobDescriptor descriptor);


    @Transactional(readOnly = true)
    @Override
    public Optional<JobDescriptor> findJob(String group, String name) {

        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey(name, group));
            if(Objects.nonNull(jobDetail))
                return Optional.of(
                        JobDescriptor.buildDescriptor(jobDetail,
                                scheduler.getTriggersOfJob(jobKey(name, group))));
        } catch (SchedulerException e) {
            log.error("Could not find job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
        }

        log.warn("Could not find job with key - {}.{}", group, name);
        return Optional.empty();
    }


    @Override
    public abstract void updateJob(String group, String name, JobDescriptor descriptor);


    @Override
    public void deleteJob(String group, String name) {
        try {
            scheduler.deleteJob(jobKey(name, group));
            log.info("Deleted job with key - {}.{}", group, name);
        } catch (SchedulerException e) {
            log.error("Could not delete job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
        }
    }


    @Override
    public void pauseJob(String group, String name) {
        try {
            scheduler.pauseJob(jobKey(name, group));
            log.info("Paused job with key - {}.{}", group, name);
        } catch (SchedulerException e) {
            log.error("Could not pause job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
        }
    }


    @Override
    public void resumeJob(String group, String name) {
        try {
            scheduler.resumeJob(jobKey(name, group));
            log.info("Resumed job with key - {}.{}", group, name);
        } catch (SchedulerException e) {
            log.error("Could not resume job with key - {}.{} due to error - {}", group, name, e.getLocalizedMessage());
        }
    }
}
