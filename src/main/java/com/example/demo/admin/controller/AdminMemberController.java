package com.example.demo.admin.controller;

import com.example.demo.admin.dto.MemberDto;
import com.example.demo.admin.model.MemberParam;
import com.example.demo.admin.model.MemberInput;
import com.example.demo.course.controller.BaseController;
import com.example.demo.member.service.MemberService;
import com.example.demo.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminMemberController extends BaseController {

    private final MemberService memberService;

    @GetMapping("/admin/member/list")
    public String list(Model model, MemberParam parameter){

        parameter.initPageListProperty();
        List<MemberDto> members = memberService.list(parameter);

        long totalCount = 0;
        if(members != null && members.size() > 0){
            totalCount = members.get(0).getTotalCount();
        }

        String queryString = parameter.getQueryString();
        String pagerHtml = super.getPaperHtml(totalCount, parameter.getPageSize()
                , parameter.getPageIndex(), queryString);

        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/member/list";
    }

    @GetMapping("/admin/member/detail")
    public String detail(Model model, MemberParam parameter){

        parameter.initPageListProperty();

        MemberDto member = memberService.detail(parameter.getUserId());
        model.addAttribute("member", member);


        return "admin/member/detail";
    }

    @PostMapping("/admin/member/status")
    public String status(Model model, MemberInput parameter){

        boolean result = memberService.updateStatus(parameter.getUserId(), parameter.getUserStatus());

        return "redirect:/admin/member/detail?userId="+parameter.getUserId();
    }
    @PostMapping("/admin/member/password")
    public String passwordReset(Model model, MemberInput parameter){

        boolean result = memberService.updatePassword(parameter.getUserId(), parameter.getPassword());

        return "redirect:/admin/member/detail?userId="+parameter.getUserId();
    }
}
