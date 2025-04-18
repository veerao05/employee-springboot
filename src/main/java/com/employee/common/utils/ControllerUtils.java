package com.employee.common.utils;

import com.employee.response.EmployeeResponse;
import com.employee.response.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utils Class for Controller
 */
public class ControllerUtils {


    /**
     * Builds OK response with status {@code HttpStatus.OK}
     *
     * @param results List<T>
     * @param <T>     type
     * @return ResponseEntity<TeamResponse < T>>
     */
    public static <T> ResponseEntity<EmployeeResponse<T>> buildOkResponse(final Page<T> results) {

        final EmployeeResponse<T> response =
                EmployeeResponse.<T>builder()
                        .data(results.getContent())
                        .pagination(buildPagination(results))
                        .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Builds pagination given search results
     *
     * @param results
     * @param <T>     type (ex: Teams/Player)
     * @return Pagination
     */
    private static <T> Pagination buildPagination(final Page<T> results) {
        final Pagination pagination = new Pagination();
        pagination.setPage(results.getNumber());
        pagination.setTotalPages(results.getTotalPages());
        pagination.setTotalResults(results.getTotalElements());
        return pagination;
    }

}
