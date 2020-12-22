package kz.springboot.javaee.hometask7.repositories;

import kz.springboot.javaee.hometask7.entities.BuyItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyItemRepository extends JpaRepository<BuyItem, Long> {
}
