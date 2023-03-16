package ru.otus.lesson22.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.lesson22.model.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Mono<Void> deleteByBookId(String book);

}
