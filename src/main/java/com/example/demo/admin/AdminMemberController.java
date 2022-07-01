package com.example.demo.admin;

import org.springframework.web.bind.annotation.GetMapping;

public class AdminMemberController {

    @GetMapping("/admin/member/list")
    public String list(){
        return "admin/member/list";
    }
}
