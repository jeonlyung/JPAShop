package jpabook.jpashop.service;

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

    //상품 단건 조회
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    //상품 전체 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }


}
