package kz.springboot.javaee.hometask7.repositories;

import kz.springboot.javaee.hometask7.entities.Brand;
import kz.springboot.javaee.hometask7.entities.Category;
import kz.springboot.javaee.hometask7.entities.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {

    List<ShopItem> findAllByAmountGreaterThanOrderById(int amount);
    List<ShopItem> findAllByInTopPageIsTrueOrderById();
    List<ShopItem> findAllByInTopPageIsFalseOrderById();
    List<ShopItem> findAllByBrandOrderById(Brand brand);
    List<ShopItem> findAllByNameLikeOrderById(String name);
    List<ShopItem> findAllByNameLikeAndPriceBetweenOrderById(String name, double price1, double price2);
    List<ShopItem> findAllByNameLikeAndBrandAndPriceBetweenOrderById(String name, Brand brand, double price, double price2);
    List<ShopItem> findAllByNameLikeAndPriceBetweenOrderByPriceAsc(String name, double price1, double price2);
    List<ShopItem> findAllByNameLikeAndPriceBetweenOrderByPriceDesc(String name, double price1, double price2);
    List<ShopItem> findAllByNameLikeAndBrandAndPriceBetweenOrderByPriceAsc(String name, Brand brand, double price1, double price2);
    List<ShopItem> findAllByNameLikeAndBrandAndPriceBetweenOrderByPriceDesc(String name, Brand brand, double price1, double price2);

    List<ShopItem> findAllByCategoriesContaining(Category category);
}
