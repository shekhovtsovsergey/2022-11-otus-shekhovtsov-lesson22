package ru.otus.lesson22.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.lesson22.controller.AuthorRestController;
import ru.otus.lesson22.converter.AuthorConverter;
import ru.otus.lesson22.dto.AuthorDto;
import ru.otus.lesson22.model.Author;
import ru.otus.lesson22.repository.AuthorRepository;
import java.util.Collections;
import java.util.List;
import static org.mockito.BDDMockito.when;



@WebFluxTest(AuthorRestController.class)
@DisplayName("Контроллер авторов")
public class AuthorRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private AuthorConverter authorConverter;

    @Test
    @DisplayName("должен возвращать список авторов")
    public void testGetAuthorList() {
        Author author = new Author();
        author.setId("1");
        author.setName("Test");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId("1");
        authorDto.setName("Test");
        List<Author> authorList = Collections.singletonList(author);
        when(authorRepository.findAll()).thenReturn(Flux.fromIterable(authorList));
        when(authorConverter.entityToDto(author)).thenReturn(authorDto);

        webTestClient.get()
                .uri("/api/v1/author")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(1)
                .contains(authorDto);
    }
}
