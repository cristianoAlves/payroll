package com.example.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class EmployeeDto {

    private Long id;
    private String name;
    private String role;

    public EmployeeDto(String name, String role) {
        this.name = name;
        this.role = role;
    }
}
