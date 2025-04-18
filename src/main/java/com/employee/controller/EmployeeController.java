package com.employee.controller;

import com.employee.common.mapper.EmployeeMapper;
import com.employee.common.utils.ControllerUtils;
import com.employee.dao.Employee;
import com.employee.dto.EmployeeDto;
import com.employee.response.EmployeeResponse;
import com.employee.service.EmployeeService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Resource
    private EmployeeMapper employeeMapper;

    @GetMapping
    public ResponseEntity<EmployeeResponse<Employee>> getAllEmployees(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Direction dir
    ) {
        return ControllerUtils.buildOkResponse(
                employeeService.findTeamWithPagination(offset, pageSize, sortBy, dir));


    }

    @GetMapping("/{id}")
    public ResponseEntity
            <Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findEmployeeById(id);
        if (employee != null) {
            return ResponseEntity.status(HttpStatus.OK).body(employee);

        } else {
            System.out.println("Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        System.out.println(employeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.saveEmployee(employeeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDto);
        if (updatedEmployee != null) {
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

