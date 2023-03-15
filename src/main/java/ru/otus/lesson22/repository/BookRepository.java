package ru.otus.lesson22.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.lesson22.exception.BookNotFoundException;
import ru.otus.lesson22.model.Author;
import ru.otus.lesson22.model.Book;
import java.util.List;


public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    List<Book> findAllByAuthor_Id(String authorId);

    boolean existsByAuthor_Id(String authorId);

    List<Book> findAllByGenre_Id(String genreId);

    boolean existsByGenre_Id(String genreId);

    List<Book> findAllByAuthor_IdAndGenre_Id(String authorId, String genreId);

    Mono<Book> findById(String id);

}
