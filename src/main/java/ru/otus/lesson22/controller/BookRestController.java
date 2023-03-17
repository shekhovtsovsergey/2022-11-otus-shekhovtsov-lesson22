package ru.otus.lesson22.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.lesson22.converter.BookConverter;
import ru.otus.lesson22.dto.BookDto;
import ru.otus.lesson22.exception.AuthorNotFoundException;
import ru.otus.lesson22.exception.BookNotFoundException;
import ru.otus.lesson22.exception.GenreNotFoundException;
import ru.otus.lesson22.model.Author;
import ru.otus.lesson22.model.Book;
import ru.otus.lesson22.model.Genre;
import ru.otus.lesson22.repository.AuthorRepository;
import ru.otus.lesson22.repository.BookRepository;
import ru.otus.lesson22.repository.CommentRepository;
import ru.otus.lesson22.repository.GenreRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookRestController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;
    private final BookConverter bookConverter;

    @GetMapping("/api/v1/book")
    public Flux<BookDto> getBookList() {
        return bookRepository.findAll()
                .map(bookConverter::entityToDto);
    }

    @GetMapping("/api/v1/book/{id}")
    public Mono<BookDto> getBookById(@PathVariable(name = "id") String id) {
        return bookRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new BookNotFoundException(id)))
                .map(bookConverter::entityToDto);
    }

    @DeleteMapping("/api/v1/book/{id}")
    public Mono<Void> deleteBookById(@PathVariable(name = "id") String id) {
        return bookRepository.deleteById(id).then(commentRepository.deleteByBookId(id));
    }

    @PutMapping("/api/v1/book/{id}")
    public Mono<BookDto> updateBook(@RequestBody BookDto bookDto) {
        Mono<Author> authorMono = authorRepository.
                findById(bookDto.getAuthor())
                .switchIfEmpty(Mono.error(new AuthorNotFoundException(bookDto.getAuthor())));
        Mono<Genre> genreMono = genreRepository.
                findById(bookDto.getGenre())
                .switchIfEmpty(Mono.error(new GenreNotFoundException(bookDto.getGenre())));
        return Mono.zip(authorMono, genreMono)
                .map(tuple -> new Book(bookDto.getId(), bookDto.getName(), tuple.getT1(), tuple.getT2(), null))
                .flatMap(bookRepository::save)
                .map(bookConverter::entityToDto);
    }

    @PostMapping("/api/v1/book")
    public Mono<BookDto> createBook(@RequestBody BookDto bookDto) {
        Mono<Author> authorMono = authorRepository
                .findById(bookDto.getAuthor())
                .switchIfEmpty(Mono.error(new AuthorNotFoundException(bookDto.getAuthor())));
        Mono<Genre> genreMono = genreRepository.
                findById(bookDto.getGenre())
                .switchIfEmpty(Mono.error(new GenreNotFoundException(bookDto.getGenre())));
        return Mono.zip(authorMono, genreMono)
                .map(tuple -> new Book(null, bookDto.getName(), tuple.getT1(), tuple.getT2(), null))
                .flatMap(bookRepository::save)
                .map(bookConverter::entityToDto);
    }

    @ExceptionHandler({BookNotFoundException.class,AuthorNotFoundException.class,GenreNotFoundException.class})
    private ResponseEntity<String> handleNotFound(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
