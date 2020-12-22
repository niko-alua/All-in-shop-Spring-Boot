package kz.springboot.javaee.hometask7.services.impl;

import kz.springboot.javaee.hometask7.entities.*;
import kz.springboot.javaee.hometask7.repositories.*;
import kz.springboot.javaee.hometask7.services.ShopItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopItemServiceImpl implements ShopItemService {

    @Autowired
    private ShopItemRepository shopItemRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private BuyItemRepository buyItemRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void addItem(ShopItem item) {
        shopItemRepository.save(item);
    }

    @Override
    public List<ShopItem> getAllItems() {
        return shopItemRepository.findAllByAmountGreaterThanOrderById(0);
    }

    @Override
    public List<ShopItem> getAllItemsInTopPage() {
        List<ShopItem> shopItems = shopItemRepository.findAllByInTopPageIsTrueOrderById();
        shopItems.addAll(shopItemRepository.findAllByInTopPageIsFalseOrderById());
        return shopItems;
    }

    @Override
    public ShopItem getItem(Long id) {
        return shopItemRepository.getOne(id);
    }

    @Override
    public void saveItem(ShopItem item) {
        shopItemRepository.save(item);
    }

    @Override
    public void deleteItem(ShopItem item) {
        shopItemRepository.delete(item);
    }

    @Override
    public List<ShopItem> getItemsByBrand(Brand brand) {
        return shopItemRepository.findAllByBrandOrderById(brand);
    }

    @Override
    public List<ShopItem> getItemsByName(String name) {
        return shopItemRepository.findAllByNameLikeOrderById(name);
    }

    @Override
    public List<ShopItem> getItemsByNamePriceBetween(String name, double p1, double p2) {
        return shopItemRepository.findAllByNameLikeAndPriceBetweenOrderById(name, p1, p2);
    }

    @Override
    public List<ShopItem> getItemsByNameBrandPriceBetween(String name, Brand brand, double p1, double p2) {
        return shopItemRepository.findAllByNameLikeAndBrandAndPriceBetweenOrderById(name, brand, p1, p2);
    }

    @Override
    public List<ShopItem> getItemsByNamePriceBetweenAsc(String name, double p1, double p2) {
        return shopItemRepository.findAllByNameLikeAndPriceBetweenOrderByPriceAsc(name, p1, p2);
    }

    @Override
    public List<ShopItem> getItemsByNamePriceBetweenDesc(String name, double p1, double p2) {
        return shopItemRepository.findAllByNameLikeAndPriceBetweenOrderByPriceDesc(name, p1, p2);
    }

    @Override
    public List<ShopItem> getItemsByNameBrandPriceBetweenAsc(String name, Brand brand, double p1, double p2) {
        return shopItemRepository.findAllByNameLikeAndBrandAndPriceBetweenOrderByPriceAsc(name, brand, p1, p2);
    }

    @Override
    public List<ShopItem> getItemsByNameBrandPriceBetweenDesc(String name, Brand brand, double p1, double p2) {
        return shopItemRepository.findAllByNameLikeAndBrandAndPriceBetweenOrderByPriceDesc(name, brand, p1, p2);
    }

    @Override
    public List<ShopItem> getItemsByCategory(Category category) {
        return shopItemRepository.findAllByCategoriesContaining(category);
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrand(Long id) {
        return brandRepository.getOne(id);
    }

    @Override
    public void addBrand(Brand brand) {
        brandRepository.save(brand);
    }

    @Override
    public void saveBrand(Brand brand) {
        brandRepository.save(brand);
    }

    @Override
    public void deleteBrand(Brand brand) {
        brandRepository.delete(brand);
    }

    @Override
    public List<Country> getAllCountry() {
        return countryRepository.findAll();
    }

    @Override
    public Country getCountry(Long id) {
        return countryRepository.getOne(id);
    }

    @Override
    public void addCountry(Country country) {
        countryRepository.save(country);
    }

    @Override
    public void saveCountry(Country country) {
        countryRepository.save(country);
    }

    @Override
    public void deleteCountry(Country country) {
        countryRepository.delete(country);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public List<Picture> getPicturesByShopItem(ShopItem item) {
        return pictureRepository.findAllByItem(item);
    }

    @Override
    public Picture getPicture(Long id) {
        return pictureRepository.getOne(id);
    }

    @Override
    public void addPicture(Picture picture) {
        pictureRepository.save(picture);
    }

    @Override
    public void savePicture(Picture picture) {
        pictureRepository.save(picture);
    }

    @Override
    public void deletePicture(Picture picture) {
        pictureRepository.delete(picture);
    }

    @Override
    public Picture getLastPicture() {
        return pictureRepository.findTopByOrderByIdDesc();
    }

    @Override
    public void buyItem(BuyItem buyItem) {
        buyItemRepository.save(buyItem);
    }

    @Override
    public List<BuyItem> boughtItems() {
        return buyItemRepository.findAll();
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getAllCommentsOfItem(ShopItem item) {
        return commentRepository.getAllByItem(item);
    }

    @Override
    public Comment getComment(Long id) {
        return commentRepository.getOne(id);
    }

    @Override
    public Comment getCommentByItem(ShopItem item) {
        return commentRepository.getByItem(item);
    }

    @Override
    public Comment getCommentByAuthor(ShopUser author) {
        return commentRepository.getByAuthor(author);
    }

    @Override
    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
