package com.example.payroll.domain.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
    value = "com.example.payroll.domain.repository"
)
@EnableTransactionManagement
@EntityScan("com.example.payroll.domain.entity")
public class JPARepositoriesAutoConfiguration {
}