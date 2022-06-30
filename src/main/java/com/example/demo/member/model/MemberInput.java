package com.example.demo.member.model;

import lombok.*;

@Data
@ToString
public class MemberInput {

    private String userId;
    private String userName;
    private String password;
    private String phone;


}
