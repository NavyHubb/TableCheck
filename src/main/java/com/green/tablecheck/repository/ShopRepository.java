package com.green.tablecheck.repository;

import com.green.tablecheck.domain.model.Shop;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByManagerId(Long managerId);

    Optional<Shop> findByName(String name);
}
