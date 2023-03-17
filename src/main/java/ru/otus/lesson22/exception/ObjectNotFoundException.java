package ru.otus.lesson22.exception;

public abstract class ObjectNotFoundException extends Exception {

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
