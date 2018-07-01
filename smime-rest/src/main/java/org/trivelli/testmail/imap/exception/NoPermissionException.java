package org.trivelli.testmail.imap.exception;


public class NoPermissionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public NoPermissionException() {
		super();
	}

	/**
	 * @param errorKey
	 */
	public NoPermissionException(String errorKey) {
		super(errorKey);
	}


	/**
	 * @param nestedException
	 */
	public NoPermissionException(Exception nestedException) {
		super(nestedException);
	}

}
