package ru.otus.lesson22.repository;

public interface CommentRepositoryCustom {

    void updateById(String id, String authorName, String comment);

}
