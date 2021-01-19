package com.rest.api.util;

import lombok.Data;

@Data
public class ResponseMessage {

    // https://devlog-wjdrbs96.tistory.com/182
    private StatusEnum status;
    private String message;
    private Object data;

    public ResponseMessage() {
        this.status = StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }

    public enum StatusEnum {

        OK(200, "OK"),
        BAD_REQUEST(400, "BAD_REQUEST"),
        UNAUTHORIZED(401,"UNAUTHORIZED"),
        NOT_FOUND(404, "NOT_FOUND"),
        INTERNAL_SERER_ERROR(500, "INTERNAL_SERVER_ERROR");

        int statusCode;
        String code;

        StatusEnum(int statusCode, String code) {
            this.statusCode = statusCode;
            this.code = code;
        }
    }

}

