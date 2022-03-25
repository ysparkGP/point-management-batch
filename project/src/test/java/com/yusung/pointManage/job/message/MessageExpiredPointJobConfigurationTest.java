package com.yusung.pointManage.job.message;

import com.yusung.pointManage.BatchTestSupport;
import com.yusung.pointManage.message.Message;
import com.yusung.pointManage.message.MessageRepository;
import com.yusung.pointManage.point.Point;
import com.yusung.pointManage.point.wallet.PointWallet;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class MessageExpiredPointJobConfigurationTest extends BatchTestSupport {

    @Autowired
    Job messageExpiredPointJob;

    @Autowired
    MessageRepository messageRepository;

    @Test
    void messageExpiredPointJob() throws Exception {
        // Given
        // 포인트 지갑을 생성, 오늘 만료된 포인트 적립 내역을 생성 (expiredDate = 어제)
        LocalDate earnDate = LocalDate.of(2022,1,1);
        LocalDate expireDate = LocalDate.of(2022,5,5);
        LocalDate notExpireDate = LocalDate.of(2025,12,31);
        PointWallet pointWallet1 = pointWalletRepository.save(
                new PointWallet("user1", BigInteger.valueOf(3000))
        );
        PointWallet pointWallet2 = pointWalletRepository.save(
                new PointWallet("user2", BigInteger.ZERO)
        );

        pointRepository.save(new Point(pointWallet2,BigInteger.valueOf(1000),earnDate,expireDate,false,true));
        pointRepository.save(new Point(pointWallet2,BigInteger.valueOf(1000),earnDate,expireDate,false,true));
        pointRepository.save(new Point(pointWallet1,BigInteger.valueOf(1000),earnDate,expireDate,false,true));
        pointRepository.save(new Point(pointWallet1,BigInteger.valueOf(1000),earnDate,expireDate,false,true));
        pointRepository.save(new Point(pointWallet1,BigInteger.valueOf(1000),earnDate,expireDate,false,true));
        pointRepository.save(new Point(pointWallet1,BigInteger.valueOf(1000),earnDate,notExpireDate));
        pointRepository.save(new Point(pointWallet1,BigInteger.valueOf(1000),earnDate,notExpireDate));
        pointRepository.save(new Point(pointWallet1,BigInteger.valueOf(1000),earnDate,notExpireDate));
        // When
        // Job 실행
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today","2022-05-06")
                .toJobParameters();
        JobExecution execution = launchJob(messageExpiredPointJob, jobParameters);

        // Then
        // 아래와 같은 메시지가 생성되어있는지 확인, 3000 포인트 만료
        // user1 은 3천원 포인트 만료, user2 는 2천원 포인트 만료 메시지
        then(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<Message> messages = messageRepository.findAll();
        then(messages).hasSize(2);
        Message message1 = messages.stream().filter(
                item -> item.getUserId().equals("user1")).findFirst().orElseGet(null);
        then(message1).isNotNull();
        then(message1.getTitle()).isEqualTo("3000 포인트 만료");
        then(message1.getContent()).isEqualTo("2022-05-06 기준 3000 포인트가 만료되었습니다.");
        Message message2 = messages.stream().filter(
                item -> item.getUserId().equals("user2")).findFirst().orElseGet(null);
        then(message2).isNotNull();
        then(message2.getTitle()).isEqualTo("2000 포인트 만료");
        then(message2.getContent()).isEqualTo("2022-05-06 기준 2000 포인트가 만료되었습니다.");

    }
}