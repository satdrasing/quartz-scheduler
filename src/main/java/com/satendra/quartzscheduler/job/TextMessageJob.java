package com.satendra.quartzscheduler.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;

import static org.springframework.util.CollectionUtils.isEmpty;


@Slf4j
@Setter
public class TextMessageJob implements Job {


    private String messageBody;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        sendTextMessage();
    }

    private void sendTextMessage() {
        log.info("Sending Message {}", messageBody);
    }
}
