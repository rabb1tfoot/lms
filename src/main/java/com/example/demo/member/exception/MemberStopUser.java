package com.example.demo.member.exception;

public class MemberStopUser extends RuntimeException {

    public MemberStopUser(String error) {
        super(error);
    }
}
