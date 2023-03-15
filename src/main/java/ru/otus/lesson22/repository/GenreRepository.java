package ru.otus.lesson22.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.lesson22.model.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

}
