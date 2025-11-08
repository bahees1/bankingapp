package com.sarujan.bankingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
public class TransactionException extends RuntimeException {
    public TransactionException(String message) {
        super(message);
    }
}
