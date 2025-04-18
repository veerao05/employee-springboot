package com.employee.exception;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class RestErrorResponse {

    private long timestamp;

    private String msg;
}
