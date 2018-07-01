package org.trivelli.testmail.imap.exception;



public class MailboxActionException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
    public MailboxActionException() {
        super();

    }

    /**
     * @param errorKey
     */
    public MailboxActionException(String errorKey) {
        super(errorKey);

    }


    /**
     * @param encapsulatedException
     */
    public MailboxActionException(Exception encapsulatedException) {
        super(encapsulatedException);

    }

}
