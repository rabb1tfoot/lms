package com.example.demo.member.service;

import com.example.demo.admin.dto.MemberDto;
import com.example.demo.admin.model.MemberParam;
import com.example.demo.member.entity.Member;
import com.example.demo.member.model.MemberInput;
import com.example.demo.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {

    boolean register(MemberInput parameter);

    boolean emailAuth(String uuid);

    boolean sendResetPassword(ResetPasswordInput parameter);

    boolean resetPassword(String uuid, String password);

    boolean checkResetPassword(String uuid);

    //관리자에서 맴버 리스트를 가져옴.
    List<MemberDto> list(MemberParam param);

    MemberDto detail(String userId);

    boolean updateStatus(String userId, String userStatus);

    boolean updatePassword(String userId, String password);
}
