package com.example.demo.admin.controller;

import com.example.demo.admin.dto.CategoryDto;
import com.example.demo.admin.dto.MemberDto;
import com.example.demo.admin.model.CategoryInput;
import com.example.demo.admin.model.MemberInput;
import com.example.demo.admin.model.MemberParam;
import com.example.demo.admin.service.CategoryService;
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
public class AdminCategoryController {


    private final CategoryService categoryService;

    @GetMapping("/admin/category/list")
    public String list(Model model, MemberParam parameter){

        List<CategoryDto> list = categoryService.list();
        model.addAttribute("list", list);

        return "admin/category/list";
    }

    @PostMapping("/admin/category/add")
    public String add(Model model, CategoryInput parameter){

        boolean result = categoryService.add(parameter.getCategoryName());

        return "redirect://localhost:8080/admin/category/list";
    }

    @PostMapping("/admin/category/delete")
    public String del(Model model, CategoryInput categoryInput){

        boolean result = categoryService.del(categoryInput.getId());
        return "redirect://localhost:8080/admin/category/list";
    }

    @PostMapping("/admin/category/update")
    public String update(Model model, CategoryInput categoryInput){

        boolean result = categoryService.update(categoryInput);
        return "redirect://localhost:8080/admin/category/list";
    }

}
