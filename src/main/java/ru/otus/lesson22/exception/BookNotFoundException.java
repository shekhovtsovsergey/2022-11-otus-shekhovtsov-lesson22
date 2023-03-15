package ru.otus.lesson22.exception;

public class BookNotFoundException extends ObjectNotFoundException {

    public BookNotFoundException(String bookId, String id) {
        super(String.format("Book id %s not found", bookId));
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
