package ru.otus.lesson22.exception;

public class AuthorNotFoundException extends ObjectNotFoundException {

    public AuthorNotFoundException(String authorId) {
        super(String.format("Author id %s not found", authorId));
    }
}
