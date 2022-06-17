package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
*  JpaRepository 사용 (기본적인 CRUD 모두 포함)
* */

public interface MemberRepository extends JpaRepository<Member, Long> {

    //메소드명 중요
    //select m from Member m where m.name = ?
    List<Member> findByName(String name);

}
