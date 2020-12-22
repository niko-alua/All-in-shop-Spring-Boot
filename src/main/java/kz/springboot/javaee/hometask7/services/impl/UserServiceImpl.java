package kz.springboot.javaee.hometask7.services.impl;

import kz.springboot.javaee.hometask7.entities.Role;
import kz.springboot.javaee.hometask7.entities.ShopUser;
import kz.springboot.javaee.hometask7.repositories.RoleRepository;
import kz.springboot.javaee.hometask7.repositories.UserRepository;
import kz.springboot.javaee.hometask7.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ShopUser myShopUser = userRepository.findByEmail(s);
        if(myShopUser !=  null) {
            return new User(myShopUser.getEmail(), myShopUser.getPassword(), myShopUser.getRoles());
        }

        throw new UsernameNotFoundException("User Not Found!");
    }

    @Override
    public ShopUser getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public ShopUser getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public boolean createUser(ShopUser shopUser) {
        ShopUser checkShopUser = userRepository.findByEmail(shopUser.getEmail());
        if(checkShopUser == null){
            Role role = roleRepository.findByName("ROLE_USER");
            if(role != null) {
                ArrayList<Role> roles = new ArrayList<>();
                roles.add(role);
                shopUser.setRoles(roles);
                shopUser.setPassword(passwordEncoder.encode(shopUser.getPassword()));
                userRepository.save(shopUser);
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveUser(ShopUser shopUser) {
        userRepository.save(shopUser);
    }

    @Override
    public boolean updateUserFullName(ShopUser shopUser) {
        ShopUser checkShopUser = userRepository.findByEmail(shopUser.getEmail());
        if(checkShopUser != null) {
            checkShopUser.setFullName(shopUser.getFullName());
            userRepository.save(checkShopUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserPassword(ShopUser shopUser, String old_password) {
        String email = shopUser.getEmail();
        ShopUser checkShopUser = userRepository.findByEmail(email);
        if(checkShopUser != null) {
            if(passwordEncoder.matches(old_password, checkShopUser.getPassword())) {
                String new_password_encoded = passwordEncoder.encode(shopUser.getPassword());
                checkShopUser.setPassword(new_password_encoded);
                userRepository.save(checkShopUser);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ShopUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRole(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public void addRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public void updateUserRoles(ShopUser shopUser) {
        ShopUser checkShopUser = userRepository.findByEmail(shopUser.getEmail());
        if(checkShopUser != null) {
            checkShopUser.setRoles(shopUser.getRoles());
            userRepository.save(checkShopUser);
        }
    }
}
