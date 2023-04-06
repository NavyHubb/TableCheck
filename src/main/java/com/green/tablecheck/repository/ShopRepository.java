package com.green.tablecheck.repository;

import com.green.tablecheck.domain.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {

}
