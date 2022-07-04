package com.example.demo.member.exception;

public class MemberNotEamilAuthException extends RuntimeException {

    public MemberNotEamilAuthException(String error) {
        super(error);
    }
}
