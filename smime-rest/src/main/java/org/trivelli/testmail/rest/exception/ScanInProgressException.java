package org.trivelli.testmail.rest.exception;

public class ScanInProgressException extends RuntimeException {
    private static final long serialVersionUID = -5027121014723838738L;

    public ScanInProgressException() {
        super();
    }

    public ScanInProgressException(String message) {
        super(message);
    }
    
}
