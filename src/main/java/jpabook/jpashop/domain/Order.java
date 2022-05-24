package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  /* 생성자 레벨 : PROTECTED */
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    /* Member테이블과 다:1 */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)  /* fetch Default EAGER이므로 무조건 LAZY로 변경, cascade : Entity에 객체가 있으면 자동으로 persist */
    @JoinColumn(name = "member_id")     /* Mapping값(Foreign Key) / 연관관계의 주인    */
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)   /* fetch Default EAGER이므로 무조건 LAZY로 변경, cascade : Entity에 객체가 있으면 자동으로 persist */
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    /* Java 8이상 부터 지원(LocalDateTime) */
    private LocalDateTime orderDate;

    private OrderStatus status; /*  주문 상태[ORDER, CANCEl] */

    //연관관계 편의 메소드(양방향 셋팅)
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메소드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비지니스 로직

    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice()  {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }


}
