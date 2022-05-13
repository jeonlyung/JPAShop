package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    public final EntityManager em;

    //주문 데이터 저장
    public void save(Order order){
        em.persist(order);
    }

    //주문 단건 조회
    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    //public List<Order> findAll(OrderSearch orderSearch){}


}
