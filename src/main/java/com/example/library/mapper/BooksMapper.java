package com.example.library.mapper;

import com.example.library.entity.Book;
import com.example.library.web.model.BookListResponse;
import com.example.library.web.model.BookResponse;
import com.example.library.web.model.UpsertBookRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@DecoratedWith(BookMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BooksMapper {

    Book requestToBook(UpsertBookRequest request);

    @Mapping(source = "bookId", target = "id")
    Book requestToBook(Long bookId, UpsertBookRequest request);

    @Mapping(source = "category.name", target = "categoryName")
    BookResponse bookToResponse(Book book);

    List<BookResponse> bookListToResponseList(List<Book> books);

    default BookListResponse bookListToBookListResponse(List<Book> books) {
        BookListResponse response = new BookListResponse();
        response.setBooks(bookListToResponseList(books));

        return response;
    }
}
