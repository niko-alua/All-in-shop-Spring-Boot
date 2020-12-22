package kz.springboot.javaee.hometask7.controllers;

import kz.springboot.javaee.hometask7.entities.*;
import kz.springboot.javaee.hometask7.services.ShopItemService;
import kz.springboot.javaee.hometask7.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Controller
@RequestMapping(value = "/allinshop_control_panel")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
public class AdminModeratorController {

    @Autowired
    private ShopItemService shopItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @Value("${file.itempic.uploadPath}")
    private String uploadPath;

    @GetMapping(value = "/items")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String admin_moder_panel_items(Model model){
        List<ShopItem> shopItems = shopItemService.getAllItems();
        model.addAttribute("shopItems", shopItems);

        List<Brand> brands = shopItemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Country> countries = shopItemService.getAllCountry();
        model.addAttribute("countries", countries);

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

        return "admin_moder_panel_items";
    }

    @GetMapping(value = "/countries")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin_panel_countries(Model model){
        List<Country> countries = shopItemService.getAllCountry();
        model.addAttribute("countries", countries);

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

        return "admin_panel_countries";
    }

    @GetMapping(value = "/brands")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin_panel_brands(Model model){
        List<Brand> brands = shopItemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Country> countries = shopItemService.getAllCountry();
        model.addAttribute("countries", countries);

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

        return "admin_panel_brands";
    }

    @GetMapping(value = "/categories")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin_panel_categories(Model model){
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

        return "admin_panel_categories";
    }

    @GetMapping(value = "/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin_panel_users(Model model){

        model.addAttribute("currentUser", getUserData());

        List<ShopUser> shopUsers = userService.getAllUsers();
        model.addAttribute("shopUsers", shopUsers);

        List<Role> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);

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

        return "admin_panel_users";
    }

    @GetMapping(value = "/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin_panel_roles(Model model){

        model.addAttribute("currentUser", getUserData());

        List<Role> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);

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

        return "admin_panel_roles";
    }

    @GetMapping(value = "/bought_items")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin_panel_boughtItems(Model model){
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

        List<BuyItem> boughtItems = shopItemService.boughtItems();
        model.addAttribute("boughtItems", boughtItems);

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'-'HH:mm");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        model.addAttribute("isoFormat", isoFormat);

        return "admin_panel_bought_items";
    }


    @GetMapping(value = "/details/{item_id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String item_details(Model model,
                               @PathVariable(name = "item_id")Long id){
        if(id!=null) {
            ShopItem shopItem = shopItemService.getItem(id);
            if(shopItem != null) {
                List<Brand> brands = shopItemService.getAllBrands();
                model.addAttribute("brands", brands);

                List<Category> categories = shopItemService.getAllCategories();
                model.addAttribute("categories", categories);

                List<Picture> pictures = shopItemService.getPicturesByShopItem(shopItem);
                if(pictures == null) {
                    pictures = new ArrayList<>();
                }
                model.addAttribute("pictures", pictures);

                model.addAttribute("item", shopItem);

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

                return "admin_moder_item_details";
            }
        }
        return admin_moder_panel_items(model);
    }

    @PostMapping(value = "/add_item")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItem(@RequestParam(name = "add_item_name") String shopItemName,
                          @RequestParam(name = "add_item_desc") String shopItemDescription,
                          @RequestParam(name = "add_item_price") double shopItemPrice,
                          @RequestParam(name = "add_item_amount") int shopItemAmount,
                          @RequestParam(name = "add_item_rating") int shopItemStars,
                          @RequestParam(name = "add_item_picture") String shopItemPictureUrl,
                          @RequestParam(name = "add_item_brand") Long brandId){
        Brand brand = shopItemService.getBrand(brandId);

        if(brand != null) {
            shopItemService.addItem(new ShopItem(null, shopItemName, shopItemDescription, shopItemPrice, shopItemAmount, shopItemStars, shopItemPictureUrl, null, false, brand, null));
        }
        return "redirect:/allinshop_control_panel/items";
    }

    @PostMapping (value = "/save_item")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String saveItem(@RequestParam(name = "save_item_id") Long id,
                           @RequestParam(name = "save_item_name") String shopItemName,
                           @RequestParam(name = "save_item_desc") String shopItemDescription,
                           @RequestParam(name = "save_item_price") double shopItemPrice,
                           @RequestParam(name = "save_item_rating") int shopItemStars,
                           @RequestParam(name = "save_item_picture") String shopItemPictureUrl,
                           @RequestParam(name = "save_item_inTopPage") boolean shopItemInTopPage,
                           @RequestParam(name = "save_item_brand") Long brandId){
        ShopItem item = shopItemService.getItem(id);
        if(item != null) {
            Brand brand = shopItemService.getBrand(brandId);
            if (brand != null) {
                item.setName(shopItemName);
                item.setDescription(shopItemDescription);
                item.setPrice(shopItemPrice);
                item.setStars(shopItemStars);
                item.setPictureUrl(shopItemPictureUrl);
                item.setInTopPage(shopItemInTopPage);
                shopItemService.saveItem(item);
            }
        }
        return "redirect:/allinshop_control_panel/items";
    }

    @PostMapping (value = "/set_category")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String setCategory(Model model,
                              @RequestParam(name = "set_category_item_id") Long item_id,
                              @RequestParam(name = "set_category_id") Long category_id,
                              @RequestParam(name = "contains_category") boolean contains_category,
                              HttpServletRequest request, HttpServletResponse response){

        ShopItem shopItem = shopItemService.getItem(item_id);
        if(shopItem != null) {
            List<Category> shopItemCategories = shopItem.getCategories();
            Category category = shopItemService.getCategory(category_id);
            if(category != null) {
                if(contains_category) {
                    shopItemCategories.remove(category);
                }
                else {
                    shopItemCategories.add(category);
                }
            }
            shopItem.setCategories(shopItemCategories);
            shopItemService.saveItem(shopItem);
        }
        return item_details(model, item_id);
    }

    @PostMapping(value = "/add_picture")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItemPicture(@RequestParam(name = "add_picture_item_id") Long item_id,
                                 @RequestParam(name = "item_picture") MultipartFile file){
        ShopItem shopItem = shopItemService.getItem(item_id);
        if(shopItem != null) {
            if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {

                try {
                    Picture last_picture = shopItemService.getLastPicture();
                    Long last_pic_id = 0L;
                    if(last_picture != null) {
                        last_pic_id = last_picture.getId();
                    }
                    String picName = DigestUtils.sha1Hex("itempic_" + shopItem.getId() + last_pic_id + "_!Picture");

                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(uploadPath + picName + ".jpg");
                    Files.write(path, bytes);

                    Picture picture = new Picture(null, picName, null, shopItem);
                    shopItemService.addPicture(picture);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:/allinshop_control_panel/details/" + item_id;
    }

    @PostMapping(value = "/delete_picture")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deleteItemPicture(Model model,
                                    @RequestParam(name = "delete_picture_item_id") Long item_id,
                                    @RequestParam(name = "delete_picture_id") Long picture_id,
                                    HttpServletRequest request, HttpServletResponse response){
        ShopItem shopItem = shopItemService.getItem(item_id);
        if(shopItem != null) {
            Picture picture = shopItemService.getPicture(picture_id);
            if(picture != null) {
                shopItemService.deletePicture(picture);
            }
        }
        return item_details(model, item_id);
    }


    @PostMapping(value = "/delete_item")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deleteItem(@RequestParam(name = "delete_item_id") Long id) {
        ShopItem item = shopItemService.getItem(id);
        if(item != null) {
            shopItemService.deleteItem(item);
        }
        return "redirect:/allinshop_control_panel/items";
    }

    @PostMapping(value = "/add_country")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addCountry(@RequestParam(name = "add_country_name") String name,
                          @RequestParam(name = "add_country_code") String code){
        shopItemService.addCountry(new Country(null, name, code));
        return "redirect:/allinshop_control_panel/countries";
    }

    @PostMapping (value = "/save_country")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String saveCountry(@RequestParam(name = "save_country_id") Long id,
                           @RequestParam(name = "save_country_name") String name,
                           @RequestParam(name = "save_country_code") String code){
        Country country = shopItemService.getCountry(id);
        if(country != null) {
            shopItemService.saveCountry(new Country(id, name, code));
        }
        return "redirect:/allinshop_control_panel/countries";
    }

    @PostMapping(value = "/delete_country")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteCountry(@RequestParam(name = "delete_country_id") Long id) {
        Country country = shopItemService.getCountry(id);
        if(country != null) {
            shopItemService.deleteCountry(country);
        }
        return "redirect:/allinshop_control_panel/countries";
    }


    @PostMapping(value = "/add_brand")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addBrand(@RequestParam(name = "add_brand_name") String name,
                             @RequestParam(name = "add_brand_country") Long countryId){
        Country country = shopItemService.getCountry(countryId);
        if(country != null) {
            shopItemService.addBrand(new Brand(null, name, country));
        }
        return "redirect:/allinshop_control_panel/brands";
    }

    @PostMapping (value = "/save_brand")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String saveBrand(@RequestParam(name = "save_brand_id") Long id,
                              @RequestParam(name = "save_brand_name") String name,
                              @RequestParam(name = "save_brand_country") Long countryId){
        Country country = shopItemService.getCountry(countryId);
        if(country != null) {
            Brand brand = shopItemService.getBrand(id);

            if(brand != null) {
                shopItemService.saveBrand(new Brand(id, name, country));
            }
        }
        return "redirect:/allinshop_control_panel/brands";
    }

    @PostMapping(value = "/delete_brand")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteBrand(@RequestParam(name = "delete_brand_id") Long id) {
        Brand brand = shopItemService.getBrand(id);

        if(brand != null) {
            shopItemService.deleteBrand(brand);
        }
        return "redirect:/allinshop_control_panel/brands";
    }


    @PostMapping(value = "/add_category")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addCategory(@RequestParam(name = "add_category_name", defaultValue = "") String categoryName,
                          @RequestParam(name = "add_category_logo_url", defaultValue = "") String categoryLogoURL){
        if(!categoryName.isEmpty() && !categoryLogoURL.isEmpty()) {
            shopItemService.addCategory(new Category(null, categoryName, categoryLogoURL));
        }
        return "redirect:/allinshop_control_panel/categories";
    }

    @PostMapping(value = "/save_category")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String saveCategory(@RequestParam(name = "save_category_id") Long id,
                              @RequestParam(name = "save_category_name", defaultValue = "") String saveCategoryName,
                              @RequestParam(name = "save_category_logo_url", defaultValue = "") String saveCategoryLogoURL){
        Category category = shopItemService.getCategory(id);
        if(category != null) {
            shopItemService.saveCategory(new Category(id, saveCategoryName, saveCategoryLogoURL));
        }
        return "redirect:/allinshop_control_panel/categories";
    }

    @PostMapping(value = "/delete_category")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteCategory(@RequestParam(name = "delete_category_id") Long id) {
        Category category = shopItemService.getCategory(id);
        if(category != null) {
            shopItemService.deleteCategory(category);
        }
        return "redirect:/allinshop_control_panel/categories";
    }

    @GetMapping(value = "/user_details/{user_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String userDetails(Model model, @PathVariable(name = "user_id")Long id) {
        ShopUser checkShopUser = userService.getUserById(id);
        if(checkShopUser != null) {

            model.addAttribute("currentUser", getUserData());

            model.addAttribute("shopUser", checkShopUser);

            List<Role> roles = userService.getAllRoles();
            roles.removeAll(checkShopUser.getRoles());
            model.addAttribute("roles", roles);

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

            return "admin_user_details";
        }
        else {
            return "redirect:/allinshop_control_panel/users";
        }
    }

    @PostMapping (value = "/set_role")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String setRole(@RequestParam(name = "user_email") String email,
                          @RequestParam(name = "role_id") Long role_id){
        ShopUser checkShopUser = userService.getUserByEmail(email);
        if(checkShopUser != null) {
            Role role = userService.getRole(role_id);
            if(role != null) {
                List<Role> roles = checkShopUser.getRoles();

                if(roles == null) {
                    roles = new ArrayList<>();
                }
                roles.add(role);

                checkShopUser.setRoles(roles);
                userService.updateUserRoles(checkShopUser);
            }
        }
        return "redirect:/allinshop_control_panel/users";
    }

    @PostMapping (value = "/unset_role")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String unsetRole(@RequestParam(name = "user_email") String email,
                            @RequestParam(name = "role_id") Long role_id){
        ShopUser checkShopUser = userService.getUserByEmail(email);
        if(checkShopUser != null) {
            Role role = userService.getRole(role_id);
            if(role != null) {

                List<Role> roles = checkShopUser.getRoles();

                if(roles == null) {
                    roles = new ArrayList<>();
                }

                roles.remove(role);

                checkShopUser.setRoles(roles);
                userService.saveUser(checkShopUser);
            }
        }
        return "redirect:/allinshop_control_panel/users";
    }

    @PostMapping(value = "/add_role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addRole(@RequestParam(name = "add_role_name", defaultValue = "") String roleName,
                          @RequestParam(name = "add_role_desc", defaultValue = "") String roleDesc){
        if(!roleName.isEmpty() && !roleDesc.isEmpty()) {
            userService.addRole(new Role(null, roleName, roleDesc));
        }
        return "redirect:/allinshop_control_panel/roles";
    }

    @PostMapping(value = "/save_role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String saveRole(@RequestParam(name = "save_role_id") Long id,
                           @RequestParam(name = "save_role_name", defaultValue = "") String saveRoleName,
                           @RequestParam(name = "save_role_desc", defaultValue = "") String saveRoleDesc){
        Role role = userService.getRole(id);
        if(role != null) {
            if(!saveRoleName.isEmpty() && !saveRoleDesc.isEmpty()) {
                role.setName(saveRoleName);
                role.setDescription(saveRoleDesc);
                userService.saveRole(role);
            }
        }
        return "redirect:/allinshop_control_panel/roles";
    }

    @PostMapping(value = "/delete_role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteRole(@RequestParam(name = "delete_role_id") Long id) {
        Role role = userService.getRole(id);
        if(role != null) {
            userService.deleteRole(role);
        }
        return "redirect:/allinshop_control_panel/roles";
    }

    @GetMapping(value = "/role_details/{role_id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String roleDetails(Model model,
                              @PathVariable(name = "role_id") Long id){
        Role role = userService.getRole(id);
        if(role != null) {
            model.addAttribute("role", role);

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

            return "admin_role_details";
        }
        return "redirect:/allinshop_control_panel/roles";
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
