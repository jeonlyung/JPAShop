package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) /* 데이터 변경시에는 꼭 Transcation 사용 해야함 / readOnly를 true로 설정하면 읽기(조회) 쿼리에서는 성능 향상 */
@RequiredArgsConstructor /* final 선언된 필드만 생성자(Constructor) 생성 */
public class MemberService {

    private final MemberRepository memberRepository;   //변경할 이유가 없기 때문에 final 선언

    /*
    일반적인 Spring Injection -> @Autowried 사용
    생성자 Injection(최근에 많이 사용) -> 생성자가 하나만 있을 경우 Spring에서 자동으로 Autowried해줌

    Lombok 기능 --> @RequiredARgsConstructor(생성자 injection 대체)
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    */

    //회원 가입 메소드
    @Transactional /* 쓰기 메소드이므로 readOnly = false(default) */
    public Long join(Member member){
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    //중복 회원 검증 메소드
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers =  memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    @Transactional(readOnly = true)
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //회원 단건 조회
    public Member findOne(Long memberId){
        return memberRepository.findById(memberId).get();
    }

    //회원 수정 메소드
    @Transactional
    public void update(Long id, String name) {//Member반환 가능하지만 업데이트와 분리 시키는게 좋다.
        //변경 감지 사용
        Member member = memberRepository.findById(id).get();
        member.setName(name);
    }
}
