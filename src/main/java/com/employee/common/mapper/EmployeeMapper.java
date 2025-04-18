package com.employee.common.mapper;

import com.employee.dao.Employee;
import com.employee.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        uses = {LocalDateMapper.class})
public interface EmployeeMapper {
    EmployeeDto EmployeeDaoToDto(Employee employee);

    Employee EmployeeDtoToDao(EmployeeDto EmployeeDto);

    @Mapping(target = "id", ignore = true)
    void updateEmployeeFromDto(EmployeeDto dto, @MappingTarget Employee employee);

}
