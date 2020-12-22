package kz.springboot.javaee.hometask7.services;

import kz.springboot.javaee.hometask7.entities.*;

import java.util.List;

public interface ShopItemService {
    void addItem(ShopItem item);
    List<ShopItem> getAllItems();
    List<ShopItem> getAllItemsInTopPage();
    ShopItem getItem(Long id);
    void saveItem(ShopItem item);
    void deleteItem(ShopItem item);
    List<ShopItem> getItemsByBrand(Brand brand);
    List<ShopItem> getItemsByName(String name);
    List<ShopItem> getItemsByNamePriceBetween(String name, double p1, double p2);
    List<ShopItem> getItemsByNameBrandPriceBetween(String name, Brand brand, double p1, double p2);
    List<ShopItem> getItemsByNamePriceBetweenAsc(String name, double p1, double p2);
    List<ShopItem> getItemsByNamePriceBetweenDesc(String name, double p1, double p2);
    List<ShopItem> getItemsByNameBrandPriceBetweenAsc(String name, Brand brand, double p1, double p2);
    List<ShopItem> getItemsByNameBrandPriceBetweenDesc(String name, Brand brand, double p1, double p2);

    List<ShopItem> getItemsByCategory(Category category);

    List<Brand> getAllBrands();
    Brand getBrand(Long id);
    void addBrand(Brand brand);
    void saveBrand(Brand brand);
    void deleteBrand(Brand brand);

    List<Country> getAllCountry();
    Country getCountry(Long id);
    void addCountry(Country country);
    void saveCountry(Country country);
    void deleteCountry(Country country);

    List<Category> getAllCategories();
    Category getCategory(Long id);
    void addCategory(Category category);
    void saveCategory(Category category);
    void deleteCategory(Category category);

    List<Picture> getPicturesByShopItem(ShopItem item);
    Picture getPicture(Long id);
    void addPicture(Picture picture);
    void savePicture(Picture picture);
    void deletePicture(Picture picture);
    Picture getLastPicture();

    void buyItem(BuyItem buyItem);
    List<BuyItem> boughtItems();

    List<Comment> getAllComments();
    List<Comment> getAllCommentsOfItem(ShopItem item);
    Comment getComment(Long id);
    Comment getCommentByItem(ShopItem item);
    Comment getCommentByAuthor(ShopUser author);
    void addComment(Comment comment);
    void saveComment(Comment comment);
    void deleteComment(Comment comment);
}
