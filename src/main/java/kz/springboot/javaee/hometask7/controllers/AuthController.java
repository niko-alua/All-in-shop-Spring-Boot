package kz.springboot.javaee.hometask7.controllers;

import kz.springboot.javaee.hometask7.entities.BasketItem;
import kz.springboot.javaee.hometask7.entities.Brand;
import kz.springboot.javaee.hometask7.entities.Category;
import kz.springboot.javaee.hometask7.entities.ShopUser;
import kz.springboot.javaee.hometask7.services.ShopItemService;
import kz.springboot.javaee.hometask7.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private ShopItemService shopItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @GetMapping(value = "/login")
    public String login(Model model,
                        @RequestParam(required = false) String error,
                        @RequestParam(required = false) String success){
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
        model.addAttribute("error", error);
        model.addAttribute("success", success);

        return "login";
    }

    @GetMapping(value = "/register")
    @PreAuthorize("isAnonymous()")
    public String register(Model model, @RequestParam(required = false) String error){
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
        model.addAttribute("error", error);
        return "registration";
    }

    @PostMapping(value = "/to_register")
    public String toRegister(@RequestParam(name = "user_email") String email,
                             @RequestParam(name = "user_password") String password,
                             @RequestParam(name = "re_user_password") String rePassword,
                             @RequestParam(name = "user_full_name") String fullName) {
        if(email!= null && !email.isEmpty() &&
                password!= null && !password.isEmpty() &&
                rePassword!= null && !rePassword.isEmpty() &&
                fullName!= null && !fullName.isEmpty()) {
            if(password.equals(rePassword)) {
                ShopUser shopUser = new ShopUser();
                shopUser.setEmail(email);
                shopUser.setPassword(password);
                shopUser.setFullName(fullName);
                if(userService.createUser(shopUser)) {
                    return "redirect:/login?success";
                }
            }
        }

        return "redirect:/register?error";
    }

    @PostMapping(value = "/update_full_name")
    public String updateFullName(@RequestParam(name = "user_email") String email,
                                 @RequestParam(name = "user_full_name", defaultValue = "") String fullName) {
        if(!fullName.isEmpty()) {
            ShopUser shopUser = new ShopUser();
            shopUser.setEmail(email);
            shopUser.setFullName(fullName);
            if(userService.updateUserFullName(shopUser)) {
                return "redirect:/profile?result=success";
            }
        }
        return "redirect:/profile?result=error";
    }

    @PostMapping(value = "/update_password")
    public String updatePassword(@RequestParam(name = "user_email") String email,
                                 @RequestParam(name = "old_user_password") String old_user_password,
                                 @RequestParam(name = "new_user_password") String new_user_password,
                                 @RequestParam(name = "re_new_user_password") String re_new_user_password) {
        if(new_user_password.equals(re_new_user_password)) {
            ShopUser shopUser = new ShopUser();
            shopUser.setEmail(email);
            shopUser.setPassword(new_user_password);
            if(userService.updateUserPassword(shopUser, old_user_password)) {
                return "redirect:/profile?result=success";
            }
        }
        return "redirect:/profile?result=error";
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
