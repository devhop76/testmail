package org.trivelli.testmail.imap.exception;

public class ConnectionException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 
     */
    public ConnectionException() {
        super();

    }
    /**
     * @param nestedException
     */
    public ConnectionException(Exception nestedException) {
        super(nestedException);
    }

    /**
     * @param errorKey
     */
    public ConnectionException(String errorKey) {
        super(errorKey);
    }
}
