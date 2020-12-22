package kz.springboot.javaee.hometask7.controllers;

import kz.springboot.javaee.hometask7.entities.*;
import kz.springboot.javaee.hometask7.services.ShopItemService;
import kz.springboot.javaee.hometask7.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ErrorController {

    @Autowired
    private ShopItemService shopItemService;

    @Autowired
    private UserService userService;

    @Autowired
    HttpSession session;

    @GetMapping(value = "/403")
    public String accessDenied(Model model){
        List<ShopItem> shopItems = shopItemService.getAllItemsInTopPage();
        model.addAttribute("shopItems", shopItems);

        List<Brand> brands = shopItemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Category> categories = shopItemService.getAllCategories();
        model.addAttribute("categories", categories);

        List<ShopItem> allShopItems = shopItemService.getAllItems();
        model.addAttribute("allShopItems", allShopItems);

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
        return "403";
    }

    private ShopUser getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            User secUser = (User)authentication.getPrincipal();
            return userService.getUserByEmail(secUser.getUsername());
        }
        return null;
    }
}
