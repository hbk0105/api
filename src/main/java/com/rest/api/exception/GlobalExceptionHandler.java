package com.rest.api.exception;

import com.rest.api.util.ResponseMessage;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // https://lottogame.tistory.com/3916

    // https://eblo.tistory.com/48
    // 자주 발생하는 예외

    // https://velog.io/@gillog/Java-%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC-Exception


    @ExceptionHandler({ BindException.class })
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public ResponseMessage bindException(HttpServletRequest req , final BindException  ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseMessage(HttpStatus.BAD_REQUEST, errors.toString(), req.getRequestURL().toString());
    }


    @ExceptionHandler({ AuthenticationException.class })
    @ResponseStatus(value=HttpStatus.UNAUTHORIZED)
    public ResponseMessage handleAccessDeniedException(HttpServletRequest req , final AuthenticationException exception) {
        return new ResponseMessage(HttpStatus.UNAUTHORIZED, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(value=HttpStatus.FORBIDDEN)
    public ResponseMessage handleAccessDeniedException(HttpServletRequest req , final AccessDeniedException exception) {
        return new ResponseMessage(HttpStatus.FORBIDDEN, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final RuntimeException exception)  {
        return new ResponseMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final Exception exception)  {
        return new ResponseMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final NotFoundException exception)  {
        return new ResponseMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final NullPointerException exception)  {
        return new ResponseMessage(HttpStatus.NOT_FOUND, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final NoResultException exception)  {
        return new ResponseMessage(HttpStatus.NOT_FOUND, exception.getMessage(), req.getRequestURL().toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public ResponseMessage exception(HttpServletRequest req, HttpServletResponse httpServletResponse , final IllegalArgumentException exception)  {
        return new ResponseMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), req.getRequestURL().toString());
    }

}
