package kz.springboot.javaee.hometask7.repositories;

import kz.springboot.javaee.hometask7.entities.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<ShopUser, Long> {
    ShopUser findByEmail(String email);
}
