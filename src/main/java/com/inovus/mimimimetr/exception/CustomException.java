package com.inovus.mimimimetr.exception;

public class CustomException extends Exception {

    public CustomException() {
        super("Попробуйте еще раз...");
    }

    public CustomException(String message) {
        super(message);
    }
}
