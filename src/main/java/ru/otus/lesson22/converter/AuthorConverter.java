package ru.otus.lesson22.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.lesson22.dto.AuthorDto;
import ru.otus.lesson22.model.Author;

@Component
@RequiredArgsConstructor
public class AuthorConverter {

    public AuthorDto entityToDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }

}
