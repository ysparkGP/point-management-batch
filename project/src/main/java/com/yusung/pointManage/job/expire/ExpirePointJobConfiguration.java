package com.yusung.pointManage.job.expire;


import com.yusung.pointManage.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpirePointJobConfiguration {

    @Bean
    public Job expirePointJob(
            JobBuilderFactory jobBuilderFactory,
            TodayJobParameterValidator validator,
            Step expirePointStep
    ){
        return jobBuilderFactory.get("expirePointJob")
                .validator(validator)
                .incrementer(new RunIdIncrementer()) // id 자동 증가시켜 중복 실행 방지
                .start(expirePointStep)
                .build();
    }
}
