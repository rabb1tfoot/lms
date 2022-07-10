package com.example.demo.course.model;

import lombok.Data;

@Data
public class CourseInput {

    String subject;
    Long id;
    Long categoryId;
    String keyword;
    String summary;
    String contents;
    long price;
    long salePrice;
    String saleEndDtText;

    //삭제
    String idList;
}
