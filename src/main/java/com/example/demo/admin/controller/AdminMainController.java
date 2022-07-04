package com.example.demo.admin.controller;

import com.example.demo.admin.dto.MemberDto;
import com.example.demo.admin.model.MemberParam;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminMainController {

    private final MemberService memberService;

    @GetMapping("/admin/main")
    public String main(){
        return "admin/main";
    }



}
