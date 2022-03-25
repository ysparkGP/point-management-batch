package com.yusung.pointManage.job.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class InputExpiredSoonPointAlarmCriteriaDateStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        // today jobParameter 를 가저온다.
        // today + 7 한 다음 alarmCriteriaDate 라는 키값으로 StepExecutionContext 에 설정
        JobParameter todayParameter = stepExecution.getJobParameters().getParameters().get("today");
        if(todayParameter == null) return;

        LocalDate today = LocalDate.parse((String) todayParameter.getValue());
        ExecutionContext context = stepExecution.getExecutionContext();
        context.put("alarmCriteriaDate", today.plusDays(7).format(DateTimeFormatter.ISO_DATE));
        stepExecution.setExecutionContext(context);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
