package ru.otus.lesson22.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.lesson22.model.Comment;

import java.util.List;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String>, CommentRepositoryCustom {

    List<Comment> findAllByBookId(String bookId);

    void deleteAllByBookId(String bookId);

}
