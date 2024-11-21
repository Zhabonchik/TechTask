package com.ifortex.bookservice.repository.impl;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Member findMember() {
        String query = "SELECT m FROM Member m " +
                "JOIN m.borrowedBooks b " +
                "WHERE array_to_string (b.genres, ',') like '%Romance%' " +
                "ORDER BY m.membershipDate ASC LIMIT 1";

        return (Member) entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Member> findMembers() {
        String query = "SELECT m FROM Member m " +
                "WHERE YEAR(m.membershipDate) = 2023 " +
                "AND not exists elements(m.borrowedBooks)";

        return entityManager.createQuery(query).getResultList();
    }
}
