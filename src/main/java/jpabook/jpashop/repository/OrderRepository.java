package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    //주문 데이터 저장
    public void save(Order order){
        em.persist(order);
    }

    //주문 단건 조회
    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    //주문 검색 조회(이름, 상태값)
    public List<Order> findAll(OrderSearch orderSearch){
        String jpql = "select o from Order o join o.member m";

        return em.createQuery("select o from Order o join o.member m" +
                "where o.status = :status" +
                "and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)  //1000개 리스트가 최대
                .getResultList();
    }

    /** 실무에서 잘 사용하지 않는다.
     * 동적쿼리 조회(에제1)
     */
    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000); //최대 1000건

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    /** 실무에서 사용하지 않는다.
     * JPA Criteria (JPA 표준 스펙)
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null){
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(criteria.toArray(new Predicate[criteria.size()]));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    /** 동적쿼리에 매우 좋음(실무에 자주 사용)
     *  !!Querydsl 라이브러리 사용!!
     */
    public List<Order> findAllDsl(OrderSearch orderSearch){
        JPAQueryFactory query = new JPAQueryFactory(em);
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();

    }

    private BooleanExpression nameLike(String memberName) {
        if(StringUtils.hasText(memberName)){
            return null;
        }
        return QMember.member.name.like(memberName);
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if(statusCond == null){
            return null; //null이면 where절 자동으로 버림
        }
        return QOrder.order.status.eq(statusCond);
    }

    /**
     * Join Fetch(객체를 다 채워서 가져옴)
     */
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o " +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
                ).getResultList();
    }

    /**
     * Join Fetch / 페이징 처리
     * toOne관계만 Fetch Join
     */
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o " +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * 성능 최적화는 되지만 코드 재사용성이 떨어진다.(딱딱함) / 데이터 변경 불가(엔티티가 아닌 Dto로 호출하기 때문에)
     */
    public List<OrderSimpleQueryDto> findOrderDtos(){
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        "from Order o" +
                        " join o.member m" +
                        " join o.delivery d",OrderSimpleQueryDto.class)
                .getResultList();
    }

    public List<Order> findAllWithItem() {//Fetch 조인해서 distinct 사용할 경우 페이징 처리 불가
        return em.createQuery(
                "select distinct o from Order o" + //JPA에서 distinct --> 엔티티 id값이 동일하면 중복 제거
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .getResultList();
    }

}
