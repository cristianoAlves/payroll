package com.example.payroll.domain.config.db;

import com.example.payroll.domain.entity.Employee;
import com.example.payroll.domain.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    private static final String PRELOADING = "Preloading {}";

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            log.info(PRELOADING, repository.save(new Employee("Bilbo Baggins", "burglar")));
            log.info(PRELOADING, repository.save(new Employee("Frodo Baggins", "thief")));
        };
    }
}
