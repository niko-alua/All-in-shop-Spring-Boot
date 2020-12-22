package kz.springboot.javaee.hometask7.repositories;

import kz.springboot.javaee.hometask7.entities.Comment;
import kz.springboot.javaee.hometask7.entities.ShopItem;
import kz.springboot.javaee.hometask7.entities.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment getByAuthor(ShopUser author);
    Comment getByItem(ShopItem item);
    List<Comment> getAllByItem(ShopItem item);
}
