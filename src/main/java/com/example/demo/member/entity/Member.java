package com.example.demo.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
public class Member implements MemberCode{

    @Id
    private String userId;
    private String userName;
    private String phoneNumber;
    private String password;
    private LocalDateTime regDt;

    private boolean isEmailAuth;
    private LocalDateTime emailAuthDt;
    private String emailAuthKey;

    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt;

    private boolean adminYN;

    private String userStatus;

}
