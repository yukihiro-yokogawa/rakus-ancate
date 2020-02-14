package com.example.demo.exception;

public class LoggedInUserIdNotMatchException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public LoggedInUserIdNotMatchException(String msg) {
		super(msg);
	}

}
