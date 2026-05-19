package com.example.demo.service.impl;

import com.example.demo.model.entity.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import com.example.demo.service.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks(String author) {
        if (author == null || author.isBlank()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public Book createBook(Book book) {
        book.setId(null);
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        book.setId(id);
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }
}
