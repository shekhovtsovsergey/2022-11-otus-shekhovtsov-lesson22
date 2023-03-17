package ru.otus.lesson22.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.lesson22.controller.GenreRestController;
import ru.otus.lesson22.converter.GenreConverter;
import ru.otus.lesson22.dto.GenreDto;
import ru.otus.lesson22.model.Genre;
import ru.otus.lesson22.repository.GenreRepository;
import java.util.Collections;
import java.util.List;
import static org.mockito.BDDMockito.when;


@WebFluxTest(GenreRestController.class)
@DisplayName("Контроллер жанров")
public class GenreRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private GenreConverter genreConverter;


    @Test
    @DisplayName("должен уметь возвращать список жанров")
    public void getGenreList_shouldReturnGenreList() {
        Genre genre = new Genre();
        genre.setId("1");
        genre.setName("Test");
        GenreDto genreDto = new GenreDto();
        genreDto.setId("1");
        genreDto.setName("Test");
        List<Genre> genreList = Collections.singletonList(genre);
        when(genreRepository.findAll()).thenReturn(Flux.fromIterable(genreList));
        when(genreConverter.entityToDto(genre)).thenReturn(genreDto);

        webTestClient.get()
                .uri("/api/v1/genre")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Genre.class)
                .hasSize(1)
                .contains();
    }
}
