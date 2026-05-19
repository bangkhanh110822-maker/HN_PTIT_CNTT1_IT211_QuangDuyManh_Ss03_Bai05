package com.example.demo.repository;

import com.example.demo.model.entity.Book;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final ConcurrentHashMap<Long, Book> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @PostConstruct
    void seed() {
        save(new Book(null, "Clean Code", "Robert C. Martin", 2008, true));
        save(new Book(null, "Effective Java", "Joshua Bloch", 2018, true));
        save(new Book(null, "Spring in Action", "Craig Walls", 2023, false));
    }

    @Override
    public List<Book> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(Book::getId))
                .toList();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Book> findByAuthorContainingIgnoreCase(String author) {
        String keyword = author == null ? "" : author.trim().toLowerCase();
        return store.values().stream()
                .filter(book -> book.getAuthor() != null && book.getAuthor().toLowerCase().contains(keyword))
                .sorted(Comparator.comparing(Book::getId))
                .toList();
    }

    @Override
    public Book save(Book book) {
        Long id = book.getId();
        if (id == null) {
            id = sequence.incrementAndGet();
            book.setId(id);
        }
        store.put(id, copy(book));
        return copy(book);
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    private Book copy(Book source) {
        return new Book(
                source.getId(),
                source.getTitle(),
                source.getAuthor(),
                source.getYear(),
                source.isAvailable()
        );
    }
}
