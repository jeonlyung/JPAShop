package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //싱글 테이블 전략(한 테이블에 다 때려 박는다)
@DiscriminatorColumn(name = "dtype") //상속 테이블 구분 값
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private long id;

    private String name;            //이름
    private int price;              //가격
    private int stockQuantity;      //재고

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

}
