package com.example.demo.admin.controller.service;

import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.model.CourseInput;
import com.example.demo.course.model.CourseParam;
import com.example.demo.course.model.ServiceResult;
import com.example.demo.course.model.TakeCourseInput;

import java.util.List;

public interface CourseService {

    boolean add(CourseInput parameter);

    List<CourseDto> list(CourseParam parameter);

    CourseDto getById(long id);

    boolean set(CourseInput parameter);

    boolean del(String idList);

    //회원용 코스 리스트
    List<CourseDto> frontList(CourseParam parameter);

    CourseDto frontDetail(long id);

    ServiceResult req(TakeCourseInput parameter);
}
