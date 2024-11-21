package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.repository.impl.BookRepositoryImpl;
import com.ifortex.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepositoryImpl bookRepository;

    @Autowired
    BookServiceImpl(BookRepositoryImpl bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public Map<String, Long> getBooks() {
        List <Object []> result = bookRepository.getBooks();

        Map<String, Long> countByGenre = new LinkedHashMap<>();

        result.forEach(object -> countByGenre.put((String) object[0], (Long) object[1]));

        return countByGenre;
    }

    @Override
    @Transactional
    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        return bookRepository.getAllByCriteria(searchCriteria);
    }
}
