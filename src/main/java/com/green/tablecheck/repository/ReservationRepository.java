package com.green.tablecheck.repository;

import com.green.tablecheck.domain.model.Reservation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByShopId(Long shopId);
    Optional<Reservation> findByShopIdAndCustomerId(Long shopId, Long customerId);
}
