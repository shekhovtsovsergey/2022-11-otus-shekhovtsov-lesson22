package ru.otus.lesson22.mongock.changelog;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.lesson22.model.Author;
import ru.otus.lesson22.model.Comment;
import ru.otus.lesson22.model.Genre;
import ru.otus.lesson22.model.Book;
import ru.otus.lesson22.repository.AuthorRepository;
import ru.otus.lesson22.repository.BookRepository;
import ru.otus.lesson22.repository.CommentRepository;
import ru.otus.lesson22.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private Author leanNielsen;
    private Author klausRifbjerg;
    private Author thorkildBjornvig;
    private Author cecilBodker;
    private Author greteStenbaek;

    private Genre documental;
    private Genre historycal;

    private Book bookOne;
    private Book bookTwo;
    private Book bookThree;
    private Book bookFour;
    private Book bookFive;


    @ChangeSet(order = "000", id = "dropDb", author = "shekhovtsov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "shekhovtsov", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        leanNielsen = repository.save(new Author(null, "Lean Nielsen")).block();
        klausRifbjerg = repository.save(new Author(null, "Klaus Rifbjerg")).block();
        thorkildBjornvig = repository.save(new Author(null, "Thorkild Bjørnvig")).block();
        cecilBodker = repository.save(new Author(null, "Cecil Bødker")).block();
        greteStenbaek = repository.save(new Author(null, "Grete Stenbæk")).block();
    }

    @ChangeSet(order = "002", id = "initGenres", author = "shekhovtsov", runAlways = true)
    public void initGenres(GenreRepository repository) {
        documental = repository.save(new Genre(null, "Documental")).block();
        historycal = repository.save(new Genre(null, "History")).block();
    }

    @ChangeSet(order = "003", id = "initBooks", author = "shekhovtsov", runAlways = true)
    public void initBooks(BookRepository bookRepository,CommentRepository commentRepository) {
        bookOne = bookRepository.save(new Book(null, "Ned ad trappen, ud på gaden (Danish Edition)", leanNielsen, documental,new ArrayList<Comment>())).block();
        bookTwo = bookRepository.save(new Book(null, "Kesses krig (Unge læsere) (Danish Edition)", leanNielsen, documental,new ArrayList<Comment>())).block();
        bookThree = bookRepository.save(new Book(null, "Hjørnestuen og månehavet: Erindringer 1934-1938 (Danish Edition)", leanNielsen, documental,new ArrayList<Comment>())).block();
        bookFour = bookRepository.save(new Book(null, "Vandgården: Roman (Danish Edition)", leanNielsen, documental,new ArrayList<Comment>())).block();
        bookFive = bookRepository.save(new Book(null, "Thea (Danish Edition)", leanNielsen, documental,new ArrayList<Comment>())).block();
    }

    @ChangeSet(order = "004", id = "initComments", author = "shekhovtsov", runAlways = true)
    public void initComments(CommentRepository commentRepository, BookRepository bookRepository) {
        List<Comment> comments = IntStream.range(0, 5)
                .mapToObj(i -> new Comment(null, "shekhovtsov", "habe nichts verstanden", bookOne))
                .collect(Collectors.toList());
        commentRepository.saveAll(comments).collectList().block();
        bookOne.getComment().addAll(comments);
        bookRepository.save(bookOne).block();
    }
}
