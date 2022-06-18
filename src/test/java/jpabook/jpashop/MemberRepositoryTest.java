package jpabook.jpashop;

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

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional           /* 트랜잭션 어노테이션(스프링) */
    @Rollback(value = false) /* false일 경우 TEST 롤백 안하고 커밋 바로 진행함 */
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setName("memberA");

        //when
        memberRepository.save(member);
        Long saveId = member.getId();
        Member findMember = memberRepository.findById(saveId).get();

        //then
        Assertions.assertEquals(findMember.getId(), member.getId());
        Assertions.assertEquals(findMember.getName(), member.getName());
        Assertions.assertEquals(findMember, member);
        
        //1차 캐시에서 기존에 관리하던 엔티티 가져옴(이론)
        System.out.println("findeMember == member : " + (findMember == member));
    }

}