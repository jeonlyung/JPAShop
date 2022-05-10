package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    //다:다 관계
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    //부모
    @ManyToOne(fetch = FetchType.LAZY)   /* fetch Default EAGER이므로 무조건 LAZY로 변경 */
    @JoinColumn(name = "parent_id")
    private  Category parent;

    //자식
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //연관 관계 편의 메소드(양방향 셋팅)
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }

}
