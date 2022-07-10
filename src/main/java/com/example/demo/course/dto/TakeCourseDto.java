package com.example.demo.course.dto;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Data
public class TakeCourseDto {

    @Id
    @GeneratedValue
    Long id;
    Long courseId;

    long payPrice;
    String status;

    LocalDateTime regDt;

    String userId;
    String userName;
    String phoneNumber;
    String subject;

    //임시
    long totalCount;
    long seq;

    public String getRegDtText(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");
        return regDt != null ? regDt.format(format) : "";
    }
}
