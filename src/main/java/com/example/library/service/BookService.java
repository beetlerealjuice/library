package com.example.library.service;

import com.example.library.entity.Book;

import java.util.List;

public interface BookService {

    Book findById(Long id);

    Book findBookByTitleAndAuthor(String title, String author);

    List<Book> findBooksByCategory(String categoryName);

    Book createBook(Book book);

    Book updateBook(Book book);

    void deleteById(Long id);

    List<Book> findAll();

}
