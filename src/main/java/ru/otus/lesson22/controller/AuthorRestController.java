package ru.otus.lesson22.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.lesson22.converter.AuthorConverter;
import ru.otus.lesson22.dto.AuthorDto;
import ru.otus.lesson22.repository.AuthorRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthorRestController {

    private final AuthorRepository authorRepository;
    private final AuthorConverter authorConverter;

    @GetMapping("/api/v1/author")
    public Flux<AuthorDto> getAuthorList() {
        return authorRepository.findAll()
                .map(authorConverter::entityToDto);
    }
}
