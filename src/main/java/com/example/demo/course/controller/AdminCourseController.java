package com.example.demo.course.controller;

import com.example.demo.admin.service.CategoryService;
import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.model.CourseInput;
import com.example.demo.course.model.CourseParam;
import com.example.demo.admin.controller.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminCourseController extends BaseController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/admin/course/list")
    public String list(Model model, CourseParam parameter){

        parameter.initPageListProperty();
        List<CourseDto> courseList = courseService.list(parameter);

        long totalCount = 0;
        if(!CollectionUtils.isEmpty(courseList)){
            totalCount = courseList.get(0).getTotalCount();
        }

        String queryString = parameter.getQueryString();
        String pagerHtml = super.getPaperHtml(totalCount, parameter.getPageSize()
                , parameter.getPageIndex(), queryString);
        model.addAttribute("list", courseList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/course/list";
    }

    @GetMapping(value = {"/admin/course/add", "/admin/course/edit" })
    public String add(Model model, HttpServletRequest request
    , CourseInput parameter){


        //카테고리 정보 필요
        model.addAttribute("category",  categoryService.list());

       boolean isEditMode = request.getRequestURI().contains("/edit");
        CourseDto detail = new CourseDto();

        if(isEditMode){
            long id = parameter.getId();

            CourseDto exitCourse = courseService.getById(id);

            if(exitCourse == null){
                //error처리
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";

            }
            detail = exitCourse;
        }
        model.addAttribute("editMode", isEditMode);
        model.addAttribute("detail", detail);

        return "admin/course/add";
    }

    @PostMapping(value = {"/admin/course/add", "/admin/course/edit"})
    public String addSubmit(Model model, HttpServletRequest request, CourseInput parameter){

        boolean isEditMode = request.getRequestURI().contains("/edit");

        if(isEditMode){
            long id = parameter.getId();

            CourseDto exitCourse = courseService.getById(id);

            if(exitCourse == null){
                //error처리
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";

            }
            boolean result = courseService.set(parameter);
        }
        else{
            boolean result = courseService.add(parameter);
        }
        return "redirect:/admin/course/list";
    }

    @PostMapping("/admin/course/delete")
    public String del(Model model, HttpServletRequest request, CourseInput parameter){

        boolean result = courseService.del(parameter.getIdList());

        return "redirect:/admin/course/list";
    }
}
