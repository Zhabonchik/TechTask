package com.ifortex.bookservice.repository;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;

import java.util.List;

public interface BookRepository {
    List<Object []> getBooks();
    List<Book> getAllByCriteria(SearchCriteria searchCriteria);
}
