package com.yusung.pointManage;

import com.yusung.pointManage.message.MessageRepository;
import com.yusung.pointManage.point.PointRepository;
import com.yusung.pointManage.point.reservation.PointReservationRepository;
import com.yusung.pointManage.point.wallet.PointWalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BatchTestSupport {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    protected PointWalletRepository pointWalletRepository;
    @Autowired
    protected PointRepository pointRepository;
    @Autowired
    protected MessageRepository messageRepository;
    @Autowired
    protected PointReservationRepository pointReservationRepository;

    protected JobExecution launchJob(Job job, JobParameters jobParameters) throws Exception{

        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(job);
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJobRepository(jobRepository);
        return jobLauncherTestUtils.launchJob(jobParameters);  // jobExecution

    }

    @AfterEach
    protected void deleteAll(){
        pointRepository.deleteAll();
        pointReservationRepository.deleteAll();
        pointWalletRepository.deleteAll();
        messageRepository.deleteAll();
    }
}
