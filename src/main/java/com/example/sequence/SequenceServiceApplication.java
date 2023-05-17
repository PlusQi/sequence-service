package com.example.sequence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class SequenceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SequenceServiceApplication.class, args);
    }

}
