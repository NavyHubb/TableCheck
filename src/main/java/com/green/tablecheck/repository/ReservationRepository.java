package com.green.tablecheck.repository;

import com.green.tablecheck.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
