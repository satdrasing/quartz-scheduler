package com.satendra.quartzscheduler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.satendra.quartzscheduler.job.TextMessageJob;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.*;

import static org.quartz.JobBuilder.newJob;

@Data
public class JobDescriptor {

    @NotBlank
    private String name;
    private String group;

    @NotEmpty
    private String messageBody;

    private Map<String, Object> data = new LinkedHashMap<>();
    @JsonProperty("triggers")
    private List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();


    public JobDescriptor setName(final String name) {
        this.name = name;
        return this;
    }

    public JobDescriptor setGroup(final String group) {
        this.group = group;
        return this;
    }

    public JobDescriptor setMessageBody(String messageBody) {
        this.messageBody = messageBody;
        return this;
    }


    public JobDescriptor setData(final Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public JobDescriptor setTriggerDescriptors(final List<TriggerDescriptor> triggerDescriptors) {
        this.triggerDescriptors = triggerDescriptors;
        return this;
    }


    @JsonIgnore
    public Set<Trigger> buildTriggers() {
        Set<Trigger> triggers = new LinkedHashSet<>();
        for (TriggerDescriptor triggerDescriptor : triggerDescriptors) {
            triggers.add(triggerDescriptor.buildTrigger());
        }

        return triggers;
    }

    public JobDetail buildJobDetail() {
        JobDataMap jobDataMap = new JobDataMap(getData());
        jobDataMap.put("messageBody", messageBody);

        return newJob(TextMessageJob.class)
                .withIdentity(getName(), getGroup())
                .usingJobData(jobDataMap)
                .build();
    }



    public static JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {

        List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

        for (Trigger trigger : triggersOfJob) {
            triggerDescriptors.add(TriggerDescriptor.buildDescriptor(trigger));
        }

        return new JobDescriptor()
                .setName(jobDetail.getKey().getName())
                .setGroup(jobDetail.getKey().getGroup())

                .setMessageBody(jobDetail.getJobDataMap().getString("messageBody"))

                // .setData(jobDetail.getJobDataMap().getWrappedMap())
                .setTriggerDescriptors(triggerDescriptors);

    }
}
