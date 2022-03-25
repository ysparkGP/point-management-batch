package com.yusung.pointManage.job.message;


import com.yusung.pointManage.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageExpireSoonPointJobConfiguration {

    @Bean
    public Job messageExpireSoonPointJob(
        JobBuilderFactory jobBuilderFactory,
        TodayJobParameterValidator validator,
        Step messageExpireSoonPointStep
    ){
        return jobBuilderFactory.get("MessageExpireSoonPointJob")
                .validator(validator)
                .incrementer(new RunIdIncrementer())
                .start(messageExpireSoonPointStep)
                .build();
    }
}
