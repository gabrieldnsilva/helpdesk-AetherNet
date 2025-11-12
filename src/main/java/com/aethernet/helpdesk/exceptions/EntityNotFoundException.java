package com.aethernet.helpdesk.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, Object id) {
        super(String.format("%s não encontrado(a) com id: %s", entityName, id));
    }
}
