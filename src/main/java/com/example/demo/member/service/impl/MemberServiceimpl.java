package com.example.demo.member.service.impl;

import com.example.demo.admin.dto.MemberDto;
import com.example.demo.admin.mapper.MemberMapper;
import com.example.demo.admin.model.MemberParam;
import com.example.demo.component.MailComponents;
import com.example.demo.member.entity.Member;
import com.example.demo.member.exception.MemberNotEamilAuthException;
import com.example.demo.member.exception.MemberStopUser;
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
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
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
    private final MemberMapper memberMapper;

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
                .password(encPassword)
                .regDt(LocalDateTime.now())
                .isEmailAuth(false)
                .emailAuthKey(uuid)
                .userStatus(Member.MEMBER_STATUS_REQ)
                .build();
        memberRepository.save(member);

        String email = parameter.getUserId();
        String subject = "fastlms ???????????? ????????? ?????????????????????.";
        String text = "<p>?????????????????? ?????? ????????? ??????????????? ????????? ???????????????</p>"
                +"<div><a href='http://localhost:8080/member/email-auth?id=" + uuid
                +"'>????????????</a></div>";
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

        member.setUserStatus(Member.MEMBER_STATUS_ING);
        member.setEmailAuth(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findById(username);

        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("??????????????? ????????????.");
        }

        Member member = optionalMember.get();

        if(!member.isEmailAuth()){
            throw  new MemberNotEamilAuthException("????????? ????????? ?????? ?????????????????????.");
        }

        if(Member.MEMBER_STATUS_STOP.equals(member.getUserStatus())){
            throw  new MemberStopUser("????????? ?????? ?????????.");
        }

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
            throw new UsernameNotFoundException("??????????????? ????????????.");
        }

        Member member = optionalMember.get();

        String uuid = UUID.randomUUID().toString();

        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));
        memberRepository.save(member);


        String email = parameter.getUserId();
        String subject = "fastlms ????????? ???????????? ????????? ???????????????..";
        String text = "<p>?????????????????? ?????? ????????? ??????????????? ???????????? ???????????? ???????????????</p>"
                +"<div><a href='http://localhost:8080/member/reset_password?id=" + uuid
                +"'>???????????? ?????????</a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean resetPassword(String uuid, String password) {

        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);

        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("??????????????? ????????????.");
        }

        Member member = optionalMember.get();
        
        if(member.getResetPasswordLimitDt() == null){
            throw new RuntimeException("????????? ????????? ????????????.");
        }
        if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("????????? ????????? ????????????.");
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
            throw new RuntimeException("????????? ????????? ????????????.");
        }
        if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("????????? ????????? ????????????.");
        }

        return true;
    }

    @Override
    @Transactional
    public List<MemberDto> list(MemberParam param) {
        List<MemberDto> list = memberMapper.selectList(param);
        long totalCount = memberMapper.selectListCount(param);

        if(!CollectionUtils.isEmpty(list)){
            int i = 0;
            for(MemberDto x : list){
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - (totalCount - param.getPageStart() - i) + 1); //x.setSeq(totalCount - param.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public MemberDto detail(String userId) {

        Optional<Member> optionalMember = memberRepository.findById(userId);

        if(!optionalMember.isPresent()){
            return null;
        }
        Member member = optionalMember.get();
        return MemberDto.of(member);
    }

    @Override
    public boolean updateStatus(String userId, String userStatus) {
        Optional<Member> optionalMember = memberRepository.findById(userId);

        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("??????????????? ????????????.");
        }

        Member member = optionalMember.get();
        member.setUserStatus(userStatus);
        memberRepository.save(member);
        return true;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);

        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("??????????????? ????????????.");
        }

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Member member = optionalMember.get();
        member.setPassword(encPassword);
        memberRepository.save(member);
        return true;
    }
}
