package com.aristurtle.megakalservice.exception;

public class InvalidVoteException extends RuntimeException {
    public InvalidVoteException(String string) {
        super(string);
    }
}
