package com.aethernet.helpdesk.exceptions;

public class DuplicateEntityException extends RuntimeException {

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String fieldName, Object value) {
        super(String.format("Já existe um registro com %s: %s", fieldName, value));
    }
}
