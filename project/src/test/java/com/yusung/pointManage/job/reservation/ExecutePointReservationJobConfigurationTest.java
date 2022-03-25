package com.yusung.pointManage.job.reservation;

import com.yusung.pointManage.BatchTestSupport;
import com.yusung.pointManage.point.Point;
import com.yusung.pointManage.point.PointRepository;
import com.yusung.pointManage.point.reservation.PointReservation;
import com.yusung.pointManage.point.reservation.PointReservationRepository;
import com.yusung.pointManage.point.wallet.PointWallet;
import com.yusung.pointManage.point.wallet.PointWalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class ExecutePointReservationJobConfigurationTest extends BatchTestSupport {

    @Autowired
    PointWalletRepository pointWalletRepository;
    @Autowired
    PointReservationRepository pointReservationRepository;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    Job executePointReservationJob;

    @Test
    void executePointReservationJob() throws Exception {
        // Given
        // point reservation 이 준비되어야함
        LocalDate earnDate = LocalDate.of(2022,1,5);
        PointWallet pointWallet = pointWalletRepository.save(
                new PointWallet(
                        "user1",
                        BigInteger.valueOf(3000)
                )
        );

        pointReservationRepository.save(
                new PointReservation(pointWallet,
                BigInteger.valueOf(1000),
                earnDate,
                10)
        );

        // When
        // executePointReservationJob 실행
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today","2022-01-05").toJobParameters();
        JobExecution jobExecution = launchJob(executePointReservationJob, jobParameters);

        // Then
        // point reservation 은 완료처리, point 적립 생성, point wallet 의 잔액 증가
        then(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<PointReservation> reservations = pointReservationRepository.findAll();
        then(reservations).hasSize(1);
        then(reservations.get(0).isExecuted()).isTrue();

        List<Point> points = pointRepository.findAll();
        then(points).hasSize(1);
        then(points.get(0).getAmount()).isEqualByComparingTo(BigInteger.valueOf(1000));
        then(points.get(0).getEarnedDate()).isEqualTo(LocalDate.of(2022,1,5));
        then(points.get(0).getExpireDate()).isEqualTo(LocalDate.of(2022,1,15));

        List<PointWallet> wallets = pointWalletRepository.findAll();
        then(wallets).hasSize(1);
        then(wallets.get(0).getAmount()).isEqualByComparingTo(BigInteger.valueOf(4000));
    }
}