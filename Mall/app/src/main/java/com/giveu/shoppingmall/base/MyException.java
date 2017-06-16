package com.giveu.shoppingmall.base;

/**
 * Created by 508632 on 2017/4/1.
 */

public class MyException extends IllegalStateException {
	public MyException() {
	}

	public MyException(String s) {
		super(s);
	}

	public MyException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyException(Throwable cause) {
		super(cause);
	}

}
