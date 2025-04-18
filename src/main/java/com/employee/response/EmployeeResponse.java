package com.employee.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse<T> {

    @Builder.Default
    private List<T> data = List.of();

    @JsonInclude(NON_NULL)
    private Pagination pagination;
}
