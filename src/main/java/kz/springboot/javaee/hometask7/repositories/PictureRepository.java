package kz.springboot.javaee.hometask7.repositories;

import kz.springboot.javaee.hometask7.entities.Picture;
import kz.springboot.javaee.hometask7.entities.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PictureRepository extends JpaRepository<Picture, Long>{
    List<Picture> findAllByItem(ShopItem item);
    Picture findTopByOrderByIdDesc();
}
