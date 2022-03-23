package com.hongru.exception;

public class HongRuException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public HongRuException(String message){
		super(message);
	}

	public HongRuException(Throwable cause)
	{
		super(cause);
	}

	public HongRuException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
