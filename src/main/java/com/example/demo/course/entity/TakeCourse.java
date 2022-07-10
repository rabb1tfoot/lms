package com.example.demo.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakeCourse implements TakeCourseCode {

    @Id
    @GeneratedValue
    Long id;
    Long courseId;
    String userId;

    long payPrice;
    String status;

    LocalDateTime regDt;


}
