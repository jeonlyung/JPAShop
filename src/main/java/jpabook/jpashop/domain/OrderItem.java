package jpabook.jpashop.domain;


import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)  /* fetch Default EAGER이므로 무조건 LAZY로 변경 */
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)  /* fetch Default EAGER이므로 무조건 LAZY로 변경 */
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;  //주문 가격
    private int count;       //주문 수량
}
