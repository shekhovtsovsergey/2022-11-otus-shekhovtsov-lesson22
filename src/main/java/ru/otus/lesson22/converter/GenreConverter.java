package ru.otus.lesson22.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.lesson22.dto.GenreDto;
import ru.otus.lesson22.model.Genre;

@Component
@RequiredArgsConstructor
public class GenreConverter {

    public GenreDto entityToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

}
