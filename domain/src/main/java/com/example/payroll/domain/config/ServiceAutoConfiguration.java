package com.example.payroll.domain.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.example.payroll.domain.service"})
public class ServiceAutoConfiguration {

}
