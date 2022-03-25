package com.yusung.pointManage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PointManagementApplication {
    public static void main(String[] args) {
        log.info("application args : " + String.join(",", args));
        SpringApplication.run(PointManagementApplication.class, args);
    }
}
