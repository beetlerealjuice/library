package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DatabaseBookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findFirstByTitleAndAuthor(String title, String author);
    List<Book> findByCategoryName(String categoryName);

}
