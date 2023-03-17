package ru.otus.lesson22.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.lesson22.controller.BookRestController;
import ru.otus.lesson22.converter.BookConverter;
import ru.otus.lesson22.dto.BookDto;
import ru.otus.lesson22.model.Author;
import ru.otus.lesson22.model.Book;
import ru.otus.lesson22.model.Genre;
import ru.otus.lesson22.repository.AuthorRepository;
import ru.otus.lesson22.repository.BookRepository;
import ru.otus.lesson22.repository.CommentRepository;
import ru.otus.lesson22.repository.GenreRepository;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;



@WebFluxTest(BookRestController.class)
@DisplayName("Контроллер книг")
public class BookRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private BookConverter bookConverter;


    @Test
    @DisplayName("должен уметь возвращать список книг")
    public void getBookList_shouldReturnBookList() {
        Book book = new Book();
        book.setId("1");
        book.setName("Test book");
        BookDto bookDto = new BookDto();
        bookDto.setId("1");
        bookDto.setName("Test book");
        List<Book> bookList = Collections.singletonList(book);
        when(bookRepository.findAll()).thenReturn(Flux.fromIterable(bookList));
        when(bookConverter.entityToDto(book)).thenReturn(bookDto);

        webTestClient.get()
                .uri("/api/v1/book")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(1)
                .contains(bookDto);
    }

    @Test
    @DisplayName("должен уметь возвращать книгу по id")
    public void getBookById_shouldReturnBookDto() {
        Book book = new Book();
        book.setId("1");
        book.setName("Test book");
        BookDto bookDto = new BookDto();
        bookDto.setId("1");
        bookDto.setName("Test book");
        when(bookRepository.findById("1")).thenReturn(Mono.just(book));
        when(bookConverter.entityToDto(book)).thenReturn(bookDto);

        webTestClient.get()
                .uri("/api/v1/book/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);
    }


    @Test
    @DisplayName("должен уметь ловить ошибки и возвращать NotFound")
    public void getBookById_shouldReturnNotFound() {
        when(bookRepository.findById("1")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/book/1")
                .exchange()
                .expectStatus().isNotFound();
    }



    @Test
    @DisplayName("должен уметь удалять книгу")
    public void testDeleteBookById() {
        String id = "123";
        given(bookRepository.deleteById(id)).willReturn(Mono.empty());
        given(commentRepository.deleteByBookId(id)).willReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/book/{id}", id)
                .exchange()
                .expectStatus().isOk();

        verify(bookRepository, times(1)).deleteById(id);
        verify(commentRepository, times(1)).deleteByBookId(id);
    }


    @Test
    @DisplayName("должен уметь обновлять книгу")
    public void testUpdateBook() {
        BookDto bookDto = new BookDto("123", "Book", "456", "789");
        Author author = new Author("456", "Author");
        Genre genre = new Genre("789", "Genre");
        Book book = new Book("123", "Book", author, genre, null);
        given(authorRepository.findById("456")).willReturn(Mono.just(author));
        given(genreRepository.findById("789")).willReturn(Mono.just(genre));
        given(bookRepository.save(book)).willReturn(Mono.just(book));
        given(bookConverter.entityToDto(book)).willReturn(bookDto);

        webTestClient.put()
                .uri("/api/v1/book/{id}", bookDto.getId())
                .body(Mono.just(bookDto), BookDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);

        verify(authorRepository, times(1)).findById("456");
        verify(genreRepository, times(1)).findById("789");
        verify(bookRepository, times(1)).save(book);
        verify(bookConverter, times(1)).entityToDto(book);
    }


    @Test
    @DisplayName("должен уметь создавать книгу")
    void createBook_shouldReturnBookDto_whenValidBookDtoProvided() {
        BookDto bookDto = new BookDto("Test id","Test Book", "authorId", "genreId");
        Author author = new Author("authorId", "Test Author");
        Genre genre = new Genre("genreId", "Test Genre");
        Book book = new Book("bookId", "Test Book", author, genre, null);
        when(authorRepository.findById(bookDto.getAuthor())).thenReturn(Mono.just(author));
        when(genreRepository.findById(bookDto.getGenre())).thenReturn(Mono.just(genre));
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(book));
        when(bookConverter.entityToDto(book)).thenReturn(bookDto);

        webTestClient.post()
                .uri("/api/v1/book")
                .bodyValue(bookDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);

        verify(authorRepository).findById(bookDto.getAuthor());
        verify(genreRepository).findById(bookDto.getGenre());
        verify(bookRepository).save(any(Book.class));
        verify(bookConverter).entityToDto(book);
    }
}
