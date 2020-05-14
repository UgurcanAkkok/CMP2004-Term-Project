package com.bau.connect;
public class InvalidMessageException extends Exception{
    /* Thrown when the message used in communication 
     * is malformatted, has unexpected parts etc.
     */
	private static final long serialVersionUID = 1L;

    InvalidMessageException() {
    }

    InvalidMessageException(String msg) {
        super(msg);
    }

}
