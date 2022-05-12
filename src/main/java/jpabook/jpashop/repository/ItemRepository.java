package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;
    
    //상품 등록
    public void save(Item item) {
        if(item.getId() == null){
            em.persist(item);
        }else{
            em.merge(item); //업데이트와 비슷하다고 이해하면 된다.
        }
    }
    
    //상품 단건 조회
    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    //상품 전체 조회
    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
    

}
