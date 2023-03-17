package ru.otus.lesson22.exception;

public class GenreNotFoundException extends ObjectNotFoundException {

    public GenreNotFoundException(String genreId) {
        super(String.format("Genre id %s not found", genreId));
    }

}
