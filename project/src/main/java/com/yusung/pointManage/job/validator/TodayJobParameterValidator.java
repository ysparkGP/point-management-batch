package com.yusung.pointManage.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class TodayJobParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if(parameters == null) throw new JobParametersInvalidException(
                "job parameter is required"
        );

        String todayStr = parameters.getString("today");
        if(todayStr == null) throw new JobParametersInvalidException(
                "job parameter is required"
        );
        try{
            LocalDate.parse(todayStr);
        } catch (DateTimeParseException e){
            throw new JobParametersInvalidException(
                    "job parameter today foramt is not valid"
            );
        }
    }
}
