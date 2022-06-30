package com.example.demo;

import com.example.demo.component.MailComponents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final MailComponents mailComponents;

    @GetMapping("/")
    public String index(){

        String email = "tjwns999@gmail.com";
        String subject = "안녕하세요 테스트2입니다.";
        String text = "<p>안녕하세요 </p> <p>반갑습니다.</p>";

        mailComponents.sendMail(email, subject, text);
        return "index";
    }

}
