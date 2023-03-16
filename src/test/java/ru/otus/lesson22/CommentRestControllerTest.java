package ru.otus.lesson22;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.lesson22.controller.CommentRestController;
import ru.otus.lesson22.converter.CommentConverter;
import ru.otus.lesson22.dto.CommentDto;
import ru.otus.lesson22.model.Book;
import ru.otus.lesson22.model.Comment;
import ru.otus.lesson22.repository.BookRepository;
import ru.otus.lesson22.repository.CommentRepository;
import java.util.Arrays;
import static org.mockito.BDDMockito.when;


@WebFluxTest(CommentRestController.class)
@DisplayName("Контроллер комментариев")
class CommentRestControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private CommentConverter commentConverter;

    @Test
    @DisplayName("должен уметь возвращать список комментариев")
    void testGetCommentList() {
        Comment comment1 = new Comment();
        comment1.setId("1");
        comment1.setComment("Comment 1");
        Comment comment2 = new Comment();
        comment2.setId("2");
        comment2.setComment("Comment 2");
        CommentDto commentDto1 = new CommentDto();
        commentDto1.setId("1");
        commentDto1.setComment("Comment 1");
        CommentDto commentDto2 = new CommentDto();
        commentDto2.setId("2");
        commentDto2.setComment("Comment 2");
        when(commentRepository.findAll()).thenReturn(Flux.just(comment1, comment2));
        when(commentConverter.entityToDto(comment1)).thenReturn(commentDto1);
        when(commentConverter.entityToDto(comment2)).thenReturn(commentDto2);
        webTestClient.get().uri("/api/v1/comment")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .contains(commentDto1, commentDto2);
    }

    @Test
    @DisplayName("должен уметь возвращать список комментариев по книге")
    void testGetAllCommentsByBook() {
        Comment comment1 = new Comment();
        comment1.setId("1");
        comment1.setComment("Comment 1");
        Comment comment2 = new Comment();
        comment2.setId("2");
        comment2.setComment("Comment 2");
        CommentDto commentDto1 = new CommentDto();
        commentDto1.setId("1");
        commentDto1.setComment("Comment 1");
        CommentDto commentDto2 = new CommentDto();
        commentDto2.setId("2");
        commentDto2.setComment("Comment 2");
        Book book = new Book();
        book.setId("1");
        book.setComment(Arrays.asList(comment1, comment2));
        when(bookRepository.findById("1")).thenReturn(Mono.just(book));
        when(commentConverter.entityToDto(comment1)).thenReturn(commentDto1);
        when(commentConverter.entityToDto(comment2)).thenReturn(commentDto2);
        webTestClient.get().uri("/api/v1/book/1/comment")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .contains(commentDto1, commentDto2);
    }
    @Test
    @DisplayName("должен уметь возвращать ошибку если книга не найдена")
    void testGetAllCommentsByBookBookNotFoundException() {
        when(bookRepository.findById("1")).thenReturn(Mono.empty());
        webTestClient.get().uri("/api/v1/book/1/comment")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("Book id 1 not found");
    }
}