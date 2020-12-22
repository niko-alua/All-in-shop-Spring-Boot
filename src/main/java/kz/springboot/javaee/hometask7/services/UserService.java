package kz.springboot.javaee.hometask7.services;

import kz.springboot.javaee.hometask7.entities.Role;
import kz.springboot.javaee.hometask7.entities.ShopUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    ShopUser getUserByEmail(String email);
    ShopUser getUserById(Long id);
    boolean createUser(ShopUser shopUser);
    void saveUser(ShopUser shopUser);
    boolean updateUserFullName(ShopUser shopUser);
    boolean updateUserPassword(ShopUser shopUser, String old_password);

    List<ShopUser> getAllUsers();

    List<Role> getAllRoles();
    Role getRole(Long id);
    void addRole(Role role);
    void saveRole(Role role);
    void deleteRole(Role role);
    Role getRoleByName(String name);

    void updateUserRoles(ShopUser checkShopUser);
}
