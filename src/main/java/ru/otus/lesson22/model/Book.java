package ru.otus.lesson22.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String id;
    private String name;
    @DBRef
    private Author author;
    @DBRef
    private Genre genre;
    @DBRef
    private List<Comment> comment;


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + name + '\'' +
                ", author=" + author +
                ", genre=" + genre +
                ", comments=" + comment +
                '}';
    }
}
