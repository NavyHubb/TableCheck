package com.green.tablecheck.domain.model;

import com.green.tablecheck.domain.type.StatusType;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    private Long id;

    private String name;
    private String description;
    private String address;
    private int tableCount;

    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @OneToOne(mappedBy = "shop")
    private Manager manager;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);

        if (reservation.getShop() != this) {
            reservation.setShop(this);
        }
    }

}
