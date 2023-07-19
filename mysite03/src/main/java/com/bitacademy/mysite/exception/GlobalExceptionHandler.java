package com.bitacademy.mysite.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public String handlerException(Exception ex) {
		//1. 로깅
		ex.printStackTrace();
		
		//2. 사과
		return "error/exception";
	}
}
