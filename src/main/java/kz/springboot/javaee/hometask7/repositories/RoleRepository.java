package kz.springboot.javaee.hometask7.repositories;

import kz.springboot.javaee.hometask7.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long > {
    Role findByName(String name);
}
