package com.ifortex.bookservice.repository;

import com.ifortex.bookservice.model.Member;

import java.util.List;

public interface MemberRepository {
    Member findMember();
    List<Member> findMembers();
}