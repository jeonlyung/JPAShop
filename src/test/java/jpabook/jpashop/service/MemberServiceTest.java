package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)    /* Junit 실행할 때 Spring이랑 같이 엮어서 실행   */
@SpringBootTest                 /* SpringBoot띄운 상태에서 테스트 진행 가능      */
@Transactional                  /* 기본적으로 Transcation Rollback 함         */
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    //@Rollback(value = false)  /* 기본적으로 Transcation Rollback 함 / value = true(default) INSERT문 실행X */
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("hsj");

        //when
        Long saveId = memberService.join(member);

        //then
        Assertions.assertEquals(member, memberRepository.findById(saveId).get());

    }

    @Test(expected = IllegalStateException.class)  /* 예외발생시 Exception처리 */
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("hsj");

        Member member2 = new Member();
        member2.setName("hsj");

        //when
        memberService.join(member1);
        memberService.join(member2);  //중복으로 인하여 예외가 발생해야 한다!!

        //then
        Assertions.fail("예외가 발생해야한다.");
    }
}