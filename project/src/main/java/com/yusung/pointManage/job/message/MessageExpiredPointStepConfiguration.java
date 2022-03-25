package com.yusung.pointManage.job.message;

import com.yusung.pointManage.job.listener.InputExpiredPointAlarmCriteriaDateStepListener;
import com.yusung.pointManage.message.Message;
import com.yusung.pointManage.point.ExpiredPointSummary;
import com.yusung.pointManage.point.PointRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.Map;

@Configuration
public class MessageExpiredPointStepConfiguration {

    @Bean
    @JobScope
    public Step messageExpiredPointStep(
          StepBuilderFactory stepBuilderFactory,
          PlatformTransactionManager transactionManager,
          InputExpiredPointAlarmCriteriaDateStepListener listener,
          RepositoryItemReader<ExpiredPointSummary> messageExpiredPointItemReader,
          ItemProcessor<ExpiredPointSummary, Message> messageExpiredPointItemProcessor,
          JpaItemWriter<Message> messageExpiredPointItemWriter
    ){
        return stepBuilderFactory.get("MessageExpiredPointStep")
                .allowStartIfComplete(true) // 중복 실행 가능
                .transactionManager(transactionManager)
                .listener(listener)
                .<ExpiredPointSummary, Message>chunk(1000)
                .reader(messageExpiredPointItemReader)
                .processor(messageExpiredPointItemProcessor)
                .writer(messageExpiredPointItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<ExpiredPointSummary> messageExpiredPointItemReader(
            PointRepository pointRepository,
            @Value("#{T(java.time.LocalDate).parse(stepExecutionContext[alarmCriteriaDate])}")
            LocalDate alarmCriteriaDate
    ){
        return new RepositoryItemReaderBuilder<ExpiredPointSummary>()
                .name("messageExpiredPointItemReader")
                .repository(pointRepository)
                .methodName("sumByExpiredDate")
                .pageSize(1000)
                .arguments(alarmCriteriaDate)
                .sorts(Map.of("pointWallet", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<ExpiredPointSummary, Message> messageExpiredPointItemProcessor(
        @Value("#{T(java.time.LocalDate).parse(jobParameters[today])}")
        LocalDate today
    ){
        return summary -> Message.expiredPointMessageInstance(
            summary.getUserId(), today, summary.getAmount()
        );
    }

    @Bean
    @StepScope
    public JpaItemWriter<Message> messageExpiredPointItemWriter(
        EntityManagerFactory entityManagerFactory
    ){
        JpaItemWriter<Message> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
