package org.trivelli.testmail.imap.exception;



public class SystemException extends Exception {
	private static final long serialVersionUID = -3342022583278439648L;

	public SystemException() {
		super();
	}

	public SystemException(String errorKey) {
		super(errorKey);
	}

	public SystemException(Exception nestedException) {
		super(nestedException);
	}

}
