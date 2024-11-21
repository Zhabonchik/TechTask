package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.repository.impl.MemberRepositoryImpl;
import com.ifortex.bookservice.service.MemberService;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepositoryImpl memberRepository;

    @Autowired
    MemberServiceImpl(MemberRepositoryImpl memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member findMember() {
        try {
            return memberRepository.findMember();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Member> findMembers() {
        try {
            return memberRepository.findMembers();
        } catch (NoResultException e) {
            return null;
        }
    }
}
