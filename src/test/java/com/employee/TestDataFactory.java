package com.employee;


import com.employee.dao.Employee;
import com.employee.dto.EmployeeDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDataFactory {

    public static EmployeeDto buildTestEmployeeDtoWithMinimalData() {
        return EmployeeDto.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    public static EmployeeDto buildTestEmployeeDtoWithFullData() {
        return EmployeeDto.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phoneNo("+1234567890")
                .jobTitle("Software Engineer")
                .department("IT")
                .dateOfJoining(LocalDate.now())
                .employmentType("Full-Time")
                .emergencyContact("+9876543210")
                .build();
    }

    public static Employee buildTestEmployeeDao() {
        return Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNo("+1234567890")
                .jobTitle("Developer")
                .department("Engineering")
                .dateOfJoining(LocalDate.now())
                .employmentType("Full-Time")
                .emergencyContact("+9876543210")
                .build();
    }

    public static Page<Employee> buildTestEmployeeDaoWithPaging() {
        List<Employee> employees = new ArrayList<>();
        employees.add(buildTestEmployeeDao());

        final PageRequest pageable = PageRequest.of(0, 1);
        return new PageImpl<>(employees, pageable, employees.size());
    }


}
