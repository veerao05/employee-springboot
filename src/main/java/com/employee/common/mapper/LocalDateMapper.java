package com.employee.common.mapper;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LocalDateMapper {
    public String asString(LocalDate date) {
        return date != null ? date.toString() : null;
    }

    public LocalDate asLocalDate(String date) {
        return date != null ? LocalDate.parse(date) : null;
    }
}