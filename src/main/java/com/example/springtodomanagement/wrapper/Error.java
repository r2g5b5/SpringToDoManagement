package com.example.springtodomanagement.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {
    private String message;
    private ErrorCodes errorCode;
    private String filedName;

    public Error(String message, ErrorCodes errorCode, String filedName) {
        this.message = message;
        this.errorCode = errorCode;
        this.filedName = filedName;
    }

    public Error(String message, ErrorCodes errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public ErrorCodes getErrorCode() {
        return errorCode;
    }

    @JsonProperty
    public String getFiledName() {
        return filedName;
    }

}

