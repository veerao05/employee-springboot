package com.employee.service;

import com.employee.common.mapper.EmployeeMapper;
import com.employee.dao.Employee;
import com.employee.dto.EmployeeDto;
import com.employee.exception.EmployeeException;
import com.employee.repository.EmployeeRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Resource
    private EmployeeMapper employeeMapper;

    public void test() {
        System.out.println("tets");
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeException("Employee not found with id " + id));
    }

    public Employee saveEmployee(EmployeeDto employeeDto) {
        return employeeRepository.save(employeeMapper.EmployeeDtoToDao(employeeDto));
    }

    public Page<Employee> findTeamWithPagination(final int offset, final int pageSize,
                                                 final String sortBy, final Sort.Direction dir) {
        return employeeRepository.findAll(PageRequest.of(offset, pageSize, Sort.by(dir, sortBy)));
    }

    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeException("Employee not found with id " + id));
    }

    public void deleteEmployeeById(Long id) {
         employeeRepository.deleteById(id);
    }

    public Employee updateEmployee(Long id, EmployeeDto employeeDto) {
        return employeeRepository.findById(id).map(existingEmployee -> {
            // Map DTO to existing entity
            employeeMapper.updateEmployeeFromDto(employeeDto, existingEmployee);
            return employeeRepository.save(existingEmployee);
        }).orElse(null);
    }
}
