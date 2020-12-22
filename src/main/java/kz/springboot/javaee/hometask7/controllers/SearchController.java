package kz.springboot.javaee.hometask7.controllers;

import kz.springboot.javaee.hometask7.entities.*;
import kz.springboot.javaee.hometask7.services.ShopItemService;
import kz.springboot.javaee.hometask7.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShopItemService shopItemService;

    @Autowired
    private HttpSession session;

    @GetMapping(value = "/detailed_search")
    public String detailed_search(Model model,
                                  @RequestParam(name = "foundItem_name", defaultValue = "") String shopItemName,
                                  @RequestParam(name = "foundItem_brand", defaultValue = "0") Long brandId,
                                  @RequestParam(name = "foundItem_price_from", defaultValue = "0") int priceFrom,
                                  @RequestParam(name = "foundItem_price_to", defaultValue = "999999") int priceTo,
                                  @RequestParam(name = "order_by", required = false) String order_by) {
        List<ShopItem> shopItems;

        if (order_by == null) {
            if(brandId == 0) {
                shopItems = shopItemService.getItemsByNamePriceBetween('%' + shopItemName + '%', priceFrom, priceTo);
            }
            else {
                Brand brand = shopItemService.getBrand(brandId);
                shopItems = shopItemService.getItemsByNameBrandPriceBetween('%' + shopItemName + '%', brand, priceFrom, priceTo);
            }
        }
        else {
            if(brandId == 0) {
                if (order_by.equals("ascending")) {
                    shopItems = shopItemService.getItemsByNamePriceBetweenAsc('%' + shopItemName + '%', priceFrom, priceTo);
                }
                else {
                    shopItems = shopItemService.getItemsByNamePriceBetweenDesc('%' + shopItemName + '%', priceFrom, priceTo);
                }
            }
            else {
                Brand brand = shopItemService.getBrand(brandId);
                if (order_by.equals("ascending")) {
                    shopItems = shopItemService.getItemsByNameBrandPriceBetweenAsc('%' + shopItemName + '%', brand, priceFrom, priceTo);
                }
                else {
                    shopItems = shopItemService.getItemsByNameBrandPriceBetweenDesc('%' + shopItemName + '%', brand, priceFrom, priceTo);
                }
            }
        }
        model.addAttribute("name", shopItemName);
        model.addAttribute("priceFrom", priceFrom);
        model.addAttribute("priceTo", priceTo);
        model.addAttribute("shopItems", shopItems);
        model.addAttribute("order_by", order_by);
        model.addAttribute("currentUser", getUserData());


        Brand brand = shopItemService.getBrand(brandId);
        model.addAttribute("brand", brand);

        List<Brand> brands = shopItemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Category> categories = shopItemService.getAllCategories();
        model.addAttribute("categories", categories);

        model.addAttribute("currentUser", getUserData());

        List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
        model.addAttribute("basket", basket);

        double total = 0;
        int total_amount = 0;
        if(basket != null) {
            for (BasketItem basketItem : basket) {
                total += basketItem.getItemPrice() * basketItem.getAmount();
                total_amount += basketItem.getAmount();
            }
        }
        model.addAttribute("total", total);
        model.addAttribute("total_amount", total_amount);

        return "detailed_search";
    }

    private ShopUser getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            org.springframework.security.core.userdetails.User secUser = (org.springframework.security.core.userdetails.User)authentication.getPrincipal();
            return userService.getUserByEmail(secUser.getUsername());
        }
        return null;
    }
}
