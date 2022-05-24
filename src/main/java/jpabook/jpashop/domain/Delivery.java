package jpabook.jpashop.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery" ,fetch = FetchType.LAZY)  /* fetch Default EAGER이므로 무조건 LAZY로 변경 */
    private Order order;

    @Embedded //내장타입
    private Address address;

    @Enumerated(EnumType.STRING) // ORDINAL Default (ORDINAL 쓰면 중간에 추가 못하므로 쓰지말고 String 쓰자)
    private DeliveryStatus status; //READY, COMP
}
