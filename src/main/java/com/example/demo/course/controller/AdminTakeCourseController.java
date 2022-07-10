package com.example.demo.course.controller;

import com.example.demo.course.dto.TakeCourseDto;
import com.example.demo.course.model.ServiceResult;
import com.example.demo.course.model.TakeCourseParam;
import com.example.demo.admin.controller.service.TakeCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminTakeCourseController extends BaseController {

    private final TakeCourseService takecourseService;

    @GetMapping("/admin/takecourse/list")
    public String list(Model model, TakeCourseParam parameter){

        parameter.initPageListProperty();
        List<TakeCourseDto> courseList = takecourseService.list(parameter);

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

        return "admin/takecourse/list";
    }

    @PostMapping("/admin/takecourse/status")
    public String status(Model model, TakeCourseParam parameter){

        ServiceResult serviceResult = takecourseService.updateStatus(parameter.getId(), parameter.getStatus());

        if(!serviceResult.isResult()){
            model.addAttribute("message", serviceResult.getMessage());
            return "common/error";
        }

        return "redirect:/admin/takecourse/list";
    }


}
