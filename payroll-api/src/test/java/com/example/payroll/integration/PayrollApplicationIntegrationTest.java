package com.example.payroll.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.payroll.adapters.outbound.persistence.employee.entity.EmployeeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PayrollIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateAndReturnEmployee() {
        EmployeeEntity emp = EmployeeEntity.builder()
            .name("Alice")
            .build();
        var response = restTemplate.postForEntity("/employees", emp, EmployeeEntity.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals("Alice", response.getBody().getName());
    }
}