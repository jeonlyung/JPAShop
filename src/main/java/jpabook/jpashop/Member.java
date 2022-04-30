package jpabook.jpashop;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter /* lombok 기능 */
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;

}
