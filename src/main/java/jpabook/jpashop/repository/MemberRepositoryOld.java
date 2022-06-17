package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository /* 자동으로 Spring Bean 생성 */
@RequiredArgsConstructor //MemberService.java 참고
public class MemberRepositoryOld {

    /*
    Spring Boot에서 지원(@persistenceContext -> @Autowired 지원가능)
    @PersistenceContext
    private EntityManager em;
     */
    private final EntityManager em;

    //멤버 저장 메소드
    public void save(Member member){
        em.persist(member);
    }

    //멤버 단건 조회 메소드
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    //멤버 다수건 조회 메소드
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList(); //JPQL 사용(Entity 대상으로 쿼리 조회), SQL과 약간 다르지만 구조는 비슷
    }

    //멤버 이름으로 조회 메소드(조건 파라미터 추가)
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class).setParameter("name", name).getResultList();
    }


}
