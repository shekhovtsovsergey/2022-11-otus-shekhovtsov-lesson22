package ru.otus.lesson22.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.lesson22.model.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

}
