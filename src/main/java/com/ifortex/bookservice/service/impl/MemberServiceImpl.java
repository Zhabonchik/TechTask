package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Member findMember() {
        String query = "SELECT m FROM Member m " +
                "JOIN m.borrowedBooks b " +
                "WHERE array_to_string (b.genres, ',') like '%Romance%' " +
                "ORDER BY m.membershipDate ASC LIMIT 1";
        try {
            return (Member) entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Member> findMembers() {
        String query = "SELECT m FROM Member m " +
                "WHERE YEAR(m.membershipDate) = 2023 " +
                "AND not exists elements(m.borrowedBooks)";
        try {
            return entityManager.createQuery(query).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
