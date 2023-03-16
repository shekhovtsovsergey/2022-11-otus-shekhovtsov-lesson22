package ru.otus.lesson22.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.lesson22.converter.CommentConverter;
import ru.otus.lesson22.dto.CommentDto;
import ru.otus.lesson22.exception.BookNotFoundException;
import ru.otus.lesson22.model.Book;
import ru.otus.lesson22.repository.BookRepository;
import ru.otus.lesson22.repository.CommentRepository;
import reactor.core.publisher.Flux;


@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentRestController {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final CommentConverter commentConverter;


    @GetMapping("/api/v1/comment")
    public Flux<CommentDto> getCommentList() {
        return commentRepository.findAll()
                .map(commentConverter::entityToDto);
    }


    @GetMapping("/api/v1/book/{id}/comment")
    public Flux<CommentDto> getAllCommentsByBook(@PathVariable(name = "id") String id) throws BookNotFoundException {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new BookNotFoundException(id)))
                .flatMapIterable(Book::getComment)
                .map(commentConverter::entityToDto);
    }


    @ExceptionHandler({BookNotFoundException.class})
    private ResponseEntity<String> handleNotFound(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
