package com.rest.api.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Setter;
import org.apache.commons.pool2.BaseObject;
import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 *
 * Description : API 응답 메시지 클래스
 *
 * Modification Information
 * 수정일			 수정자						수정내용
 * -----------	-----------------------------  -------
 * 2021. 3.  22.    MICHAEL						최초작성
 *
 */
@Setter
@Getter
public class ResponseMessage extends BaseObject {
    // https://eblo.tistory.com/48
    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_KEY = "result";
    private int code;
    private boolean status;
    private String message;
    private Date timestamp;
    private Map<Object, Object> data;
    private ErrorMessage error;

    public ResponseMessage() {
        this(HttpStatus.OK);
    }

    public ResponseMessage(HttpStatus httpStatus) {
        this.data = new HashMap<>();
        this.code = httpStatus.value();
        this.status = (httpStatus.isError())? false:true;
        this.message = httpStatus.getReasonPhrase();
        this.timestamp = new Date();
    }

    public ResponseMessage(HttpStatus httpStatus, String msg, String referedUrl) {
        this.data = new HashMap<>();
        this.code = httpStatus.value();
        this.status = (httpStatus.isError())? false:true;
        this.message = httpStatus.getReasonPhrase();
        this.error = new ErrorMessage(httpStatus.value(), msg, referedUrl);
        this.timestamp = new Date();
    }

    public ResponseMessage(HttpStatus status, Object result) {
        this(status);
        this.data.put(DEFAULT_KEY, result);
    }

    public void add(String key, Object result) {
        this.data.put(key, result);
    }

    public void remove(String key) {
        this.data.remove(key);
    }


    @Getter
    @Setter
    public class ErrorMessage extends BaseObject {

        private static final long serialVersionUID = 1L;

        private int code;
        private String errorMessage;
        private String referedUrl;

        public ErrorMessage(int code, String errorMessage, String referedUrl) {
            super();
            this.code = code;
            this.errorMessage = errorMessage;
            this.referedUrl = referedUrl;
        }
    }


    public enum StatusEnum {

        OK(200, "OK"),
        BAD_REQUEST(400, "BAD_REQUEST"),
        UNAUTHORIZED(401,"UNAUTHORIZED"),
        FORBIDDEN(403,"FORBIDDEN"),
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