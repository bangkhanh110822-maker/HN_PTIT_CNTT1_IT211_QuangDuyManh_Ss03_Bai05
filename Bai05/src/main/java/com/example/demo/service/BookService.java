package com.example.demo.service;

import com.example.demo.model.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks(String author);

    Book getBookById(Long id);

    Book createBook(Book book);

    Book updateBook(Long id, Book book);

    void deleteBook(Long id);
}
