package com.aristurtle.megakalservice.exception;

public class VoteNotFoundException extends RuntimeException {
    public VoteNotFoundException(String message) {
        super(message);
    }
}
