package com.example.library.mapper;

import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.repository.DatabaseCategoryRepository;
import com.example.library.web.model.UpsertBookRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BookMapperDelegate implements BooksMapper {

    @Autowired
    private DatabaseCategoryRepository categoryRepository;

    @Override
    public Book requestToBook(UpsertBookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        // Попытка найти категорию по имени
        Category existingCategory = categoryRepository.findByName(request.getCategoryName())
                .orElseGet(() -> {
                    // Если категория не найдена, создаем новую
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategoryName());
                    return categoryRepository.save(newCategory); // Сохраняем новую категорию
                });
        book.setCategory(existingCategory);
        return book;
    }

    @Override
    public Book requestToBook(Long bookId, UpsertBookRequest request) {
        Book book = requestToBook(request);
        book.setId(bookId);
        return book;
    }
}
