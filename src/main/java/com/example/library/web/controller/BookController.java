package com.example.library.web.controller;

import com.example.library.mapper.BooksMapper;
import com.example.library.service.DatabaseBookService;
import com.example.library.web.model.BookListResponse;
import com.example.library.web.model.BookResponse;
import com.example.library.web.model.UpsertBookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final DatabaseBookService service;

    private final BooksMapper mapper;

    @GetMapping("/find")
    public ResponseEntity<BookResponse> findBook(@RequestParam String title, @RequestParam String author) {
        return ResponseEntity.ok(
                mapper.bookToResponse(service.findBookByTitleAndAuthor(title, author))
        );
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<BookListResponse> findBooksByCategory(@PathVariable String name) {
        return ResponseEntity.ok(
                mapper.bookListToBookListResponse(service.findBooksByCategory(name))
        );
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody UpsertBookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.bookToResponse(service.createBook(mapper.requestToBook(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable("id") Long bookId, @RequestBody UpsertBookRequest request) {
        return ResponseEntity.ok(
                mapper.bookToResponse(service.updateBook(mapper.requestToBook(bookId, request)))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    private ResponseEntity<BookListResponse> findAllBooks() {
        return ResponseEntity.ok(
                mapper.bookListToBookListResponse(service.findAll())
        );
    }




}
