package com.green.tablecheck.domain.model;

import com.green.tablecheck.domain.form.SignUpForm;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Manager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id")
    private Long id;

    private String email;
    private String name;
    private String password;
    private String phone;

    // cascade 속성을 통해 ShopRepository에 따로 shop객체를 save하지 않아도
    // manager 객체를 영속화할 때 이에 연관되어 있는 shop 객체까지 같이 영속화시킴
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public static Manager from(SignUpForm form) {
        return Manager.builder()
            .email(form.getEmail())
            .name(form.getName())
            .password(form.getPassword())
            .phone(form.getPhone())
            .build();
    }

    @Transactional
    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public boolean hasShop() {
        if (this.shop == null) {
            return false;
        } else {
            return true;
        }
    }

}
