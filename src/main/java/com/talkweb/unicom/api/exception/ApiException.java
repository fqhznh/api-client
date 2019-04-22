package com.talkweb.unicom.api.exception;

public class ApiException extends RuntimeException {

    private String code;

    public ApiException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public ApiException(String code, String msg, Throwable t) {
        super(msg, t);
        this.code = code;
    }

    @Override
    public String getMessage() {
        if(code == null) {
            code = "8000";
        }
        return "[" + code + "] " + super.getMessage();
    }
}
