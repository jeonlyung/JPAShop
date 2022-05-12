package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
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
    private Long id;

    private String name;            //이름
    private int price;              //가격
    private int stockQuantity;      //재고

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //비지니스 로직
    //객체지향적인 설계 -> 데이터를 가지고있는 엔티티에 데이터 변화에 대한 비지니스 메소드가 있는게 응집력이 좋다!
    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity =  restStock;
    }

}
