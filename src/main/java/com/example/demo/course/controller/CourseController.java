package com.example.demo.course.controller;

import com.example.demo.admin.dto.CategoryDto;
import com.example.demo.admin.service.CategoryService;
import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.model.CourseParam;
import com.example.demo.admin.controller.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CourseController extends BaseController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/course")
    public String list(Model model
            , CourseParam parameter){

        List<CourseDto> list = courseService.frontList(parameter);
        model.addAttribute("list", list);

        List<CategoryDto> categoryDtoList = categoryService.frontList(CategoryDto.builder().build());
        model.addAttribute("categoryList", categoryDtoList);

        int courseCount = 0;
        if(categoryDtoList != null){
            for(CategoryDto x : categoryDtoList){
                courseCount += x.getCourseCount();
            }
        }

        model.addAttribute("courseTotalCount", courseCount);

        return "course/index";
    }

    @GetMapping("/course/{id}")
    public String courseDetail(Model model
            , CourseParam parameter){

        CourseDto detail = courseService.frontDetail(parameter.getId());
        model.addAttribute("detail", detail);
        return "course/detail";
    }


}
