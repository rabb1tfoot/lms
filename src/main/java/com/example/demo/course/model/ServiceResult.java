package com.example.demo.course.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceResult {

    boolean result;
    String message;

    public ServiceResult(boolean result, String messages) {
        this.result = result;
        this.message = messages;
    }

    public ServiceResult(boolean result) {
        this.result = result;
    }
}
