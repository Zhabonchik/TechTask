package com.ifortex.bookservice.controller;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.service.MemberService;
import com.ifortex.bookservice.service.impl.MemberServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/members")
@AllArgsConstructor
public class MemberController {

  private final MemberServiceImpl memberService;

  @GetMapping("amateur")
  public Member findMember() {
    return memberService.findMember();
  }

  @GetMapping
  public List<Member> findMembers() {
    return memberService.findMembers();
  }
}
