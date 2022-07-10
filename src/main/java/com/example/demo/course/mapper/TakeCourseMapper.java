package com.example.demo.course.mapper;

import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.dto.TakeCourseDto;
import com.example.demo.course.model.CourseParam;
import com.example.demo.course.model.TakeCourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TakeCourseMapper {

    long selectListCount(TakeCourseParam parameter);
    List<TakeCourseDto> selectList(TakeCourseParam parameter);

}
