package com.example.demo.course.mapper;

import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.model.CourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {

    long selectListCount(CourseParam parameter);
    List<CourseDto> selectList(CourseParam parameter);

}
