package com.example.demo.member.controller;

import com.example.demo.member.entity.Member;
import com.example.demo.member.model.MemberInput;
import com.example.demo.member.model.ResetPasswordInput;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;


    @GetMapping(value = "/member/login")
    public String login(){
        return "member/login";
    }

    @PostMapping(value = "/member/login")
    public String loginSubmit(Model model, ResetPasswordInput parameter){
        try{
            memberService.loadUserByUsername(parameter.getUserId());
        }catch (Exception e){
        }
        return "index";
    }

    @GetMapping("/member/find_password")
    public String findPassword(){
        return "member/find_password";
    }

    @PostMapping("/member/find_password")
    public String findPasswordSubmit(Model model, ResetPasswordInput parameter){

        boolean result = false;
        try{
            result = memberService.sendResetPassword(parameter);
            model.addAttribute("result", result);
        }catch (Exception e){
        }
        return "member/find_password_result";
    }

    @RequestMapping(value = "/member/register", method = RequestMethod.GET)
    public String register(){
        return "member/register";
    }

    @RequestMapping(value = "/member/register", method = RequestMethod.POST)
    public String registerSubmit(Model model, HttpServletRequest request
            , HttpServletResponse response, MemberInput parameter){

        boolean result = memberService.register(parameter);
        model.addAttribute("result", result);
        return "member/register-complete";
    }

    @GetMapping("/member/email-auth")
    public String emailAuth(Model model, HttpServletRequest request){
        String uuid = request.getParameter("id");

        boolean result = memberService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "member/email-auth";
    }

    @GetMapping("/member/reset_password")
    public String resetPassword(Model model, HttpServletRequest request){

        String uuid = request.getParameter("id");
        model.addAttribute("uuid", uuid);

        boolean result = memberService.checkResetPassword(uuid);

        model.addAttribute("result", result);

        return "member/reset_password";
    }

    @PostMapping("/member/reset_password")
    public String resetPasswordSubmit(Model model, ResetPasswordInput parameter){

        boolean result = false;
        try{
            result =  memberService.resetPassword(parameter.getId(), parameter.getPassword());
        }catch (Exception e){

        }

        model.addAttribute("result", result);
        return "member/reset_password_result";
    }

    @GetMapping("member/info")
    public String memberInfo(){
        return "member/info";
    }
}
