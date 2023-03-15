package ru.otus.lesson22.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.lesson22.dto.BookDto;
import ru.otus.lesson22.model.Book;

@Component
@RequiredArgsConstructor
public class BookConverter {

    public BookDto entityToDto(Book book) {
        return new BookDto(book.getId(), book.getName(),book.getAuthor().getId(),book.getGenre().getId());
    }

}
