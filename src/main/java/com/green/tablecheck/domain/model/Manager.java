package com.green.tablecheck.domain.model;

import com.green.tablecheck.domain.form.SignUpForm;
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

    @OneToOne
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

}
