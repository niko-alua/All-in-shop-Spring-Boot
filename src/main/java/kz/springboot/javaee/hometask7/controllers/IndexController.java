package kz.springboot.javaee.hometask7.controllers;

import kz.springboot.javaee.hometask7.entities.*;
import kz.springboot.javaee.hometask7.services.ShopItemService;
import kz.springboot.javaee.hometask7.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Controller
public class IndexController {

    @Autowired
    private ShopItemService shopItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;

    @Value("${file.itempic.viewPath}")
    private String itemViewPath;

    @Value("${file.itempic.defaultPicture}")
    private String itemDefaultPicture;

    @GetMapping (value = "/")
    public String index(Model model){
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

        return "index";
    }

    @GetMapping(value = "/details/{id}")
    public String details(Model model,
                          @PathVariable(name = "id") Long id) {
        ShopItem item = shopItemService.getItem(id);
        model.addAttribute("item", item);

        List<Brand> brands = shopItemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Category> categories = shopItemService.getAllCategories();
        model.addAttribute("categories", categories);

        model.addAttribute("currentUser", getUserData());

        List<Picture> pictures = shopItemService.getPicturesByShopItem(item);
        if(pictures == null) {
            pictures = new ArrayList<>();
        }
        model.addAttribute("pictures", pictures);

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

        List<Comment> comments = shopItemService.getAllCommentsOfItem(item);
        model.addAttribute("comments", comments);

        boolean is_admin_or_moder = false;
        if(getUserData() != null) {
            List<Role> roles = getUserData().getRoles();
            if (roles != null) {
                for (Role role : roles) {
                    if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_MODERATOR")) {
                        is_admin_or_moder = true;
                    }
                }
            }
        }

        model.addAttribute("is_admin_or_moder", is_admin_or_moder);

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'-'HH:mm");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        model.addAttribute("isoFormat", isoFormat);

        return "details";
    }

    @GetMapping(value = "/view_item_pic/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] view_item_pic(@PathVariable(name = "url") String url) throws IOException {
        String pictureUrl = itemViewPath + "-1/" + itemDefaultPicture;

        if(url != null && !url.equals("null")) {
            pictureUrl = itemViewPath + url + ".jpg";
        }

        InputStream in;

        try {

            ClassPathResource resource = new ClassPathResource(pictureUrl);
            in = resource.getInputStream();

        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(itemViewPath + "-1/"  + itemDefaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/category")
    public String categoryItems(Model model,
                                @RequestParam(name = "cat_id")Long category_id){
        Category category = shopItemService.getCategory(category_id);

        if(category != null) {
            model.addAttribute("category", category);

            List<ShopItem> shopItems = shopItemService.getItemsByCategory(category);
            model.addAttribute("shopItems", shopItems);

            List<Brand> brands = shopItemService.getAllBrands();
            model.addAttribute("brands", brands);

            List<Category> categories = shopItemService.getAllCategories();
            model.addAttribute("categories", categories);

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

            return "category_items";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, @RequestParam(required = false) String result) {
        model.addAttribute("result", result);
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
        return "profile";
    }

    @PostMapping(value = "/upload_avatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam(name = "user_avatar")MultipartFile file) {
        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {

            try {
                ShopUser currentUser = getUserData();

                String picName = DigestUtils.sha1Hex("avatar_" + currentUser.getId() + "_!Picture");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + ".jpg");
                Files.write(path, bytes);

                currentUser.setPictureUrl(picName);
                userService.saveUser(currentUser);
                return "redirect:/profile?result=success";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "redirect:/profile?result=error";
    }

    @GetMapping(value = "/view_picture/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] viewUserAvatar(@PathVariable(name = "url") String url) throws IOException {
        String pictureUrl = viewPath + defaultPicture;

        if(url != null && !url.equals("null")) {
            pictureUrl = viewPath + url + ".jpg";
        }

        InputStream in;

        try {

            ClassPathResource resource = new ClassPathResource(pictureUrl);
            in = resource.getInputStream();

        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/basket")
    public String basket(Model model) {
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


        return "basket";
    }

    @PostMapping(value = "/add_to_basket")
    public String addToBasket(@RequestParam(name = "item_id") Long item_id,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        Cookie []cookies = request.getCookies();

        for(Cookie c : cookies) {
            if(c.getName().equals("JSESSIONID")) {
                c.setMaxAge(60*30);
                response.addCookie(c);
            }
        }
        ShopItem shopItem = shopItemService.getItem(item_id);
        if(shopItem != null) {
            if(session.getAttribute("basket") == null) {
                List<BasketItem> basket = new ArrayList<>();
                basket.add(new BasketItem(shopItem.getId(), shopItem.getName(), shopItem.getPrice(), 1));
                session.setAttribute("basket", basket);
            }
            else {
                List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
                int index = itemExists(item_id, basket);
                if(index == -1) {
                    basket.add(new BasketItem(shopItem.getId(), shopItem.getName(), shopItem.getPrice(), 1));
                }
                else {
                    int amount = basket.get(index).getAmount() + 1;
                    basket.get(index).setAmount(amount);
                }
                session.setAttribute("basket", basket);
            }
        }
        return "redirect:/basket";
    }

    @PostMapping(value = "/decrease_amount")
    public String decreaseItemAmount(@RequestParam(name = "item_id") Long item_id){
        ShopItem shopItem = shopItemService.getItem(item_id);
        if(shopItem != null) {
            List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
            int index = itemExists(item_id, basket);
            if(index != -1) {
                int amount = basket.get(index).getAmount() - 1;
                if(amount > 0) {
                    basket.get(index).setAmount(amount);
                }
                else {
                    basket.remove(index);
                }
            }
            session.setAttribute("basket", basket);
        }
        return "redirect:/basket";
    }

    @PostMapping(value = "/check_in")
    public String checkIn(HttpServletRequest request) {
        if(session.getAttribute("basket") != null) {
            List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
            for (BasketItem basketItem : basket) {
                ShopItem shopItem = shopItemService.getItem(basketItem.getItemId());
                if (shopItem != null) {
                    BuyItem buyItem = new BuyItem(null, shopItem, basketItem.getAmount(), null);
                    shopItemService.buyItem(buyItem);
                    int prev_amount = shopItem.getAmount();
                    int current_amount = prev_amount - basketItem.getAmount();
                    shopItem.setAmount(current_amount);
                    shopItemService.saveItem(shopItem);
                }
            }
            request.getSession(false).removeAttribute("basket");
        }
        return "redirect:/basket";
    }

    @PostMapping(value = "/clear_basket")
    public String clearBasket(HttpServletRequest request) {
        if(session.getAttribute("basket") != null) {
            request.getSession(false).removeAttribute("basket");
        }
        return "redirect:/";
    }

    @GetMapping(value = "/add_comment")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@RequestParam(name = "comment_text") String comment_text,
                             @RequestParam(name = "comment_item") Long item_id,
                             @RequestParam(name = "comment_author") Long author_id){
        ShopItem item = shopItemService.getItem(item_id);
        ShopUser author = userService.getUserById(author_id);
        if(item != null && author != null) {
            Comment comment = new Comment(null, comment_text, null, item, author);
            shopItemService.saveComment(comment);
        }
        return "redirect:/details/" + item_id;
    }

    @PostMapping(value = "/edit_comment")
    public String editComment(@RequestParam(name = "edited_comment") Long comment_id,
                              @RequestParam(name = "edited_comment_text") String comment_text,
                              @RequestParam(name = "edited_comment_item") Long item_id,
                              @RequestParam(name = "edited_comment_author") Long author_id){
        Comment comment = shopItemService.getComment(comment_id);
        ShopItem item = shopItemService.getItem(item_id);
        ShopUser author = userService.getUserById(author_id);
        if(comment != null && item != null && author != null) {
            comment = new Comment(comment_id, comment_text, null, item, author);
            shopItemService.saveComment(comment);
        }
        return "redirect:/details/" + item_id;
    }

    @PostMapping(value = "/delete_comment")
    public String deleteComment(@RequestParam(name="item") Long item_id,
                                @RequestParam(name = "comment") Long comment_id){
        Comment comment = shopItemService.getComment(comment_id);
        if(comment_id != null) {
            shopItemService.deleteComment(comment);
        }
        return "redirect:/details/" + item_id;
    }


    private int itemExists(Long id, List<BasketItem> basket) {
        for(int i = 0; i < basket.size(); i++) {
            if(basket.get(i).getItemId().equals(id)) {
                return i;
            }
        }
        return -1;
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
