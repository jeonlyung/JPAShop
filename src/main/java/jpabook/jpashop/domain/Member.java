package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter /* lombok 기능 */
public class Member {

    @Id @GeneratedValue /* 시퀀스 값 */
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded /* 내장 타입 할 경우 @Embedded 또는 @Embeddable이 있으면 된다.(하지만 알아 보기 좋게 둘다 적어놓는다. */
    private Address address;

    @JsonIgnore /* JsonData에서 제외됨(API호출시 주문목록 안보이게) */
    @OneToMany(mappedBy = "member") /* Order테이블과 1:다 / 맵핑된 거울일 뿐 (주인x) */
    private List<Order> orders = new ArrayList<>();

}
