package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional /* 쓰기 메소드이므로 @Transcational 선언 */
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    /**
     * 변경 감지 update예제
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        //변경 감지(영속 상태)
        Item findItem = itemRepository.findOne(itemId); //영속 상태
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        //영속 상태이므로 Transcation 상태에서 변한된 값들을 flush해줌
    }

    /**
     * 병합(merge) update예제
     */
    @Transactional
    public Item mergeItem(Long itemId, Book param){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        return findItem;
    }


    //상품 단건 조회
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    //상품 전체 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }


}
