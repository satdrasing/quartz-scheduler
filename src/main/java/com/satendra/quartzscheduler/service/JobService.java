package com.satendra.quartzscheduler.service;

import com.satendra.quartzscheduler.model.JobDescriptor;

import java.util.Optional;

public interface JobService {

    JobDescriptor createJob(String group, JobDescriptor descriptor);


    Optional<JobDescriptor> findJob(String group, String name);


    void updateJob(String group, String name, JobDescriptor descriptor);


    void deleteJob(String group, String name);


    void pauseJob(String group, String name);


    void resumeJob(String group, String name);

}

