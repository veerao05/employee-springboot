package com.employee.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.employee.common.mapper.EmployeeMapper;
import com.employee.dao.Employee;
import com.employee.dto.EmployeeDto;
import com.employee.exception.EmployeeException;
import com.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeDto employeeDto;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("john.doe@example.com");
        employeeDto.setPhoneNo("+1234567890");
        employeeDto.setJobTitle("Software Engineer");
        employeeDto.setDepartment("IT");
        employeeDto.setDateOfJoining(LocalDate.now());
        employeeDto.setEmploymentType("Full-Time");
        employeeDto.setEmergencyContact("+9876543210");

        employee = new Employee();
        employee.setId(1L);
        // Set other fields similarly
    }

    @Test
    void testGetEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployee(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmployeeException.class, () -> employeeService.getEmployee(1L));
    }

    @Test
    void testSaveEmployee() {
        when(employeeMapper.EmployeeDtoToDao(employeeDto)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.saveEmployee(employeeDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFindTeamWithPagination() {
        Page<Employee> page = new PageImpl<>(Arrays.asList(employee));
        when(employeeRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Employee> result = employeeService.findTeamWithPagination(0, 10, "id", Sort.Direction.ASC);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.findEmployeeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFindEmployeeByIdNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmployeeException.class, () -> employeeService.findEmployeeById(1L));
    }

    @Test
    void testDeleteEmployeeById() {
        doNothing().when(employeeRepository).deleteById(1L);

        assertDoesNotThrow(() -> employeeService.deleteEmployeeById(1L));

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.updateEmployee(1L, employeeDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(employeeMapper, times(1)).updateEmployeeFromDto(employeeDto, employee);
    }

    @Test
    void testUpdateEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Employee result = employeeService.updateEmployee(1L, employeeDto);

        assertNull(result);
    }
}
