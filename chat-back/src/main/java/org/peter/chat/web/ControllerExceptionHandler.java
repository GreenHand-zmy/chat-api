package org.peter.chat.web;

import lombok.extern.slf4j.Slf4j;
import org.peter.chat.exception.BusinessException;
import org.peter.chat.utils.ResultBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    // 当发生内部异常,比如用户访问不存在的api接口
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        log.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(new ResultBean<>().failed(HttpStatus.NOT_FOUND.getReasonPhrase()),
                HttpStatus.NOT_FOUND);
        //return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    // 处理业务异常
    @ExceptionHandler
    public ResponseEntity<Object> handleBusinessException(BusinessException e) {
        return new ResponseEntity<>(new ResultBean<>().failed(e.getMessage()),
                HttpStatus.OK);
    }

    // 处理服务器运行时异常
    @ExceptionHandler
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(new ResultBean<>().failed(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 处理服务器其他异常
    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception e) {
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(new ResultBean<>().failed(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
