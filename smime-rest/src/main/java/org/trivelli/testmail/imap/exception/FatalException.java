package org.trivelli.testmail.imap.exception;


public class FatalException extends Exception {
	private static final long serialVersionUID = -1410578487969099271L;
	
	public FatalException() {
		super();
	}

	public FatalException(String errorKey) {
		super(errorKey);
	}

	public FatalException(Exception nestedException) {
		super(nestedException);
	}
}
