package ru.otus.lesson22.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.lesson22.model.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

}
