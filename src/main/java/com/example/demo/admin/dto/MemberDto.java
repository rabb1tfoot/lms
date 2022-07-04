package com.example.demo.admin.dto;

import com.example.demo.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberDto {

    String userId;
    String userName;
    String phoneNumber;
    String password;
    LocalDateTime regDt;

    String emailAuth_key;
    LocalDateTime emailAuthDt;
    boolean isEmailAuth;

    String resetPasswordKey;
    LocalDateTime resetPasswordLimitDt;

    boolean adminYN;
    String userStatus;

    //임시
    long totalCount;
    long seq;

    public static MemberDto of(Member member){
        return MemberDto.builder()
                .userId(member.getUserId())
                .userName(member.getUserName())
                .phoneNumber(member.getPhoneNumber())
                //.password(member.getPassword())
                .regDt(member.getRegDt())
                .isEmailAuth(member.isEmailAuth())
                .emailAuthDt(member.getEmailAuthDt())
                .emailAuth_key(member.getEmailAuthKey())
                .resetPasswordKey(member.getResetPasswordKey())
                .resetPasswordLimitDt(member.getResetPasswordLimitDt())
                .adminYN(member.isAdminYN())
                .userStatus(member.getUserStatus())
                .build();

    }
}
