package com.example.demo.admin.controller.service;

import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.dto.TakeCourseDto;
import com.example.demo.course.model.*;

import java.util.List;

public interface TakeCourseService {

    List<TakeCourseDto> list(TakeCourseParam parameter);

    ServiceResult updateStatus(long id, String status);
}
