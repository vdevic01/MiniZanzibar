package com.example.MiniZanzibarBack.exception;

public class UserAlreadyExistsException extends CustomException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
