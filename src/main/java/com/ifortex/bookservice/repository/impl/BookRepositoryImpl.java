package com.ifortex.bookservice.repository.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> getBooks() {
        String query = "SELECT genre, COUNT(*) AS number_of_books " +
                "FROM (SELECT UNNEST(genre) AS genre FROM BOOKS) AS expanded_genres " +
                "GROUP BY genre ORDER BY number_of_books DESC";
        return entityManager.createNativeQuery(query).getResultList();
    }

    @Override
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
