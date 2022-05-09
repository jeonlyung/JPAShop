package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne                          /* Member테이블과 다:1      */
    @JoinColumn(name = "member_id")     /* Mapping값(Foreign Key) / 연관관계의 주인 */
    private Member member;

    private List<OrderItem> orderItems = new ArrayList<>();

    private Delivery delivery;

    /* Java 8이상 부터 지원(LocalDateTime) */
    private LocalDateTime orderDate;

    private OrderStatus status; /*  주문 상태[ORDER, CANCEl] */
}
