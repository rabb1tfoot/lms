package com.example.demo.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

public interface TakeCourseCode {

    String STATUS_REQ = "REQ"; //수강신청중
    String STATUS_COMPLETE = "COMPLETE"; //수강신청완료
    String STATUS_CANCEL = "CANCEL"; //수강신청취소
}
