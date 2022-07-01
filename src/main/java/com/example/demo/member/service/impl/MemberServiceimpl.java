package com.example.demo.member.service.impl;

import com.example.demo.component.MailComponents;
import com.example.demo.member.entity.Member;
import com.example.demo.member.exception.MemberNotEamilAuthException;
import com.example.demo.member.model.MemberInput;
import com.example.demo.member.model.ResetPasswordInput;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceimpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

    @Override
    public boolean register(MemberInput parameter) {

        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        if(optionalMember.isPresent()){
            return false;
        }

        String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .phoneNumber(parameter.getPhone())
                .password(parameter.getPassword())
                .regDt(LocalDateTime.now())
                .isEmailAuth(false)
                .emailAuthKey(uuid)
                .build();
        memberRepository.save(member);

        String email = parameter.getUserId();
        String subject = "fastlms 사이트에 가입을 요청하셨습니다.";
        String text = "<p>계속하시려면 아래 링크를 클릭하셔서 가입을 완료하세요</p>"
                +"<div><a href='http://localhost:8080/member/email-auth?id=" + uuid
                +"'>가입완료</a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean emailAuth(String uuid) {

        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);

        if(!optionalMember.isPresent()){
            return false;
        }

        Member member = optionalMember.get();

        if(member.isEmailAuth()){
            return false;
        }

        member.setEmailAuth(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findById(username);

        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원정보가 없습니다.");
        }

        Member member = optionalMember.get();

        if(!member.isEmailAuth()){
            throw  new MemberNotEamilAuthException("이메일 활성화 이후 로그인해주세요.")
;        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if(member.isAdminYN()){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }

    @Override
    public boolean sendResetPassword(ResetPasswordInput parameter){

        Optional<Member> optionalMember = memberRepository.findByUserIdAndUserName(
                parameter.getUserId(), parameter.getUserName());

        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원정보가 없습니다.");
        }

        Member member = optionalMember.get();

        String uuid = UUID.randomUUID().toString();

        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));
        memberRepository.save(member);


        String email = parameter.getUserId();
        String subject = "fastlms 사이트 비밀번호 초기화 메일입니다..";
        String text = "<p>계속하시려면 아래 링크를 클릭하셔서 비밀번호 초기화를 완료하세요</p>"
                +"<div><a href='http://localhost:8080/member/reset_password?id=" + uuid
                +"'>비밀번호 초기화</a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean resetPassword(String uuid, String password) {

        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);

        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원정보가 없습니다.");
        }

        Member member = optionalMember.get();
        
        if(member.getResetPasswordLimitDt() == null){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }
        if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }


        String encpassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encpassword);
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);

        if(!optionalMember.isPresent()){
            return false;
        }

        Member member = optionalMember.get();

        if(member.getResetPasswordLimitDt() == null){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }
        if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        return true;
    }
}
