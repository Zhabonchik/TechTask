package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Map<String, Long> getBooks() {
        String query = "SELECT genre, COUNT(*) AS number_of_books " +
                "FROM (SELECT UNNEST(genre) AS genre FROM BOOKS) AS expanded_genres " +
                "GROUP BY genre ORDER BY number_of_books DESC";

        List <Object []> result = entityManager.createNativeQuery(query).getResultList();

        Map<String, Long> countByGenre = new LinkedHashMap<>();

        result.forEach(object -> countByGenre.put((String) object[0], (Long) object[1]));

        return countByGenre;
    }

    @Override
    @Transactional
    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> book = query.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        if (searchCriteria.getTitle() != null && !searchCriteria.getTitle().isBlank()) {
            predicates.add(cb.like(cb.lower(book.get("title")), "%" + searchCriteria.getTitle().toLowerCase() + "%"));
        }
        if (searchCriteria.getAuthor() != null && !searchCriteria.getAuthor().isBlank()) {
            predicates.add(cb.like(cb.lower(book.get("author")), "%" + searchCriteria.getAuthor().toLowerCase() + "%"));
        }
        if (searchCriteria.getGenre() != null && !searchCriteria.getGenre().isBlank()) {
            predicates.add(cb.like(cb.lower(cb.function("array_to_string", String.class, book.get("genres"), cb.literal(","))), "%" + searchCriteria.getGenre().toLowerCase() + "%"));
        }
        if (searchCriteria.getYear() != null) {
            predicates.add(cb.equal(cb.function("date_part", Integer.class, cb.literal("year"), book.get("publicationDate")), searchCriteria.getYear()));
        }
        if (searchCriteria.getDescription() != null && !searchCriteria.getDescription().isBlank()) {
            predicates.add(cb.like(cb.lower(book.get("description")), "%" + searchCriteria.getDescription().toLowerCase() + "%"));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.asc(book.get("publicationDate")));

        return entityManager.createQuery(query).getResultList();
    }
}
