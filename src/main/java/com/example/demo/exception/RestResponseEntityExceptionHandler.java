package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
		ex.printStackTrace();
		Map<String, String> res = this.responseMessage("You are not allowed to perform this operation.");
        return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
	
	@ExceptionHandler({ Unauthorized.class })
    public ResponseEntity<Object> handleU(Exception ex, WebRequest request) {
		Map<String, String> res = this.responseMessage("You are not allowed to perform this operation.");
        return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
	
	private Map<String, String> responseMessage(String message){
		Map<String, String> res = new HashMap<>();
		res.put("message", message);
		return res;
	}
}
