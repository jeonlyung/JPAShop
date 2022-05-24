package jpabook.jpashop.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  /* 생성자 레벨 : PROTECTED */
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)  /* fetch Default EAGER이므로 무조건 LAZY로 변경 */
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)  /* fetch Default EAGER이므로 무조건 LAZY로 변경 */
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;  //주문 가격
    private int count;       //주문 수량

    //==생성 메소드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        //Item 엔티티에 가격 컬럼이 있지만, 할인 및 쿠폰으로 인하여 orderPrice가격이 변동될 수 있으므로 따로 파라미터 가져가는것이 좋다.
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    //취소 메소드
    public void cancel(){//재고수량 RollBack
        getItem().addStock(count);
    }

    //총 주문가격 조회 메소드
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
