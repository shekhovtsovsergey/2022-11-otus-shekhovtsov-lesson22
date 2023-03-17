package ru.otus.lesson22.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.lesson22.converter.GenreConverter;
import ru.otus.lesson22.dto.GenreDto;
import ru.otus.lesson22.repository.GenreRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GenreRestController {

    private final GenreConverter genreConverter;
    private final GenreRepository genreRepository;

    @GetMapping("/api/v1/genre")
    public Flux<GenreDto> getGenreList(){
        return genreRepository.findAll()
                .map(genreConverter::entityToDto);
    }
}
