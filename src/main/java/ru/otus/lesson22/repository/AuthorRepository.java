package ru.otus.lesson22.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.lesson22.model.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    Flux<Author> findAll();
    Mono<Author> findById(String id);
    Mono<Author> save(Mono<Author> authorMono);
    Flux<Author> findByName(String name);
}
