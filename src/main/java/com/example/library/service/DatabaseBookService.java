package com.example.library.service;

import com.example.library.configuration.properties.AppCacheProperties;
import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.exception.EntityNotFoundException;
import com.example.library.repository.DatabaseBookRepository;
import com.example.library.repository.DatabaseCategoryRepository;
import com.example.library.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheManager = "redisCacheManger")
@Slf4j
public class DatabaseBookService implements BookService {

    private final DatabaseBookRepository bookRepository;

    private final DatabaseCategoryRepository categoryRepository;

    @Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_BOOKS_BY_TITLE_AND_AUTHOR, key = "#title + #author")
    @Override
    public Book findBookByTitleAndAuthor(String title, String author) {
        log.info("Поиск книги по названию {} и автору {}", title, author);
        return bookRepository.findFirstByTitleAndAuthor(title, author)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Книга с названием {0} и автором {1} не найдена", title, author)));
    }

    @Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_BOOKS_BY_CATEGORY_NAME, key = "#categoryName")
    @Override
    public List<Book> findBooksByCategory(String categoryName) {
        log.info("Поиск книг по категории {}", categoryName);
        return bookRepository.findByCategoryName(categoryName);
    }

    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.DATABASE_BOOKS_BY_TITLE_AND_AUTHOR, key = "#book.title + #book.author", beforeInvocation = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DATABASE_BOOKS_BY_CATEGORY_NAME, key = "#book.category.name" , beforeInvocation = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DATABASE_BOOKS, allEntries = true, beforeInvocation = true)
    })
    @Override
    public Book createBook(Book book) {
        log.info("Создание книги с названием {} и автором {}", book.getTitle(), book.getAuthor());
        log.info("Инвалидация кэша для книги с названием '{}' и автором '{}'", book.getTitle(), book.getAuthor());
        log.info("Инвалидация кэша для категории {}", book.getCategory().getName());
        log.info("Инвалидация кэша для всех книг");

        String categoryName = book.getCategory().getName();
        // Попытка найти категорию по имени
        Category existingCategory = categoryRepository.findByName(categoryName)
                .orElseGet(() -> {
                    // Если категория не найдена, создаем новую
                    Category newCategory = new Category();
                    newCategory.setName(categoryName);
                    return categoryRepository.save(newCategory); // Сохраняем новую категорию
                });
        // Устанавливаем найденную или созданную категорию в книгу
        book.setCategory(existingCategory);

        return bookRepository.save(book);
    }

    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.DATABASE_BOOKS_BY_TITLE_AND_AUTHOR, key = "#book.title + #book.author", beforeInvocation = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DATABASE_BOOKS_BY_CATEGORY_NAME, key = "#book.category.name" , beforeInvocation = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.DATABASE_BOOKS, allEntries = true, beforeInvocation = true)
    })
    @Override
    public Book updateBook(Book book) {
        log.info("Инвалидация кэша для книги с названием '{}' и автором '{}'", book.getTitle(), book.getAuthor());
        log.info("Инвалидация кэша для категории {}", book.getCategory().getName());
        log.info("Инвалидация кэша для всех книг");
        log.info("Обновление книги с названием {} и автором {}", book.getTitle(), book.getAuthor());
        Category category = categoryRepository.findByName(book.getCategory().getName())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Категория с именем {0} не найдена!", book.getCategory().getName())));

        Book existedBook = findById(book.getId());
        BeanUtils.copyNonNullProperties(book, existedBook);
        existedBook.setCategory(category);

        return bookRepository.save(existedBook);
    }

    @CacheEvict(value = AppCacheProperties.CacheNames.DATABASE_BOOKS, allEntries = true, beforeInvocation = true)
    @Override
    public void deleteById(Long id) {
        log.info("Инвалидация кэша для всех книг");
        log.info("Удаление книги с ID {}", id);
        bookRepository.deleteById(id);
    }

    @Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_BOOKS)
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Книга с ID {0} не найдена!", id)));
    }
}
