package ru.kata.spring.boot_security.demo.service;


import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    @Query("SELECT u FROM User u")
    public List<User> getAllUsers();
    public User getUserById(long id);
    public void deleteUserById(long id);
    void deleteUserByName(String name);
    public void updateUser(User user);
    public void addUser(User user);
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByName(String username);
    void createRolesIfNotExist();

    List<Role> getListOfRoles();
    Set<GrantedAuthority> getAuthorities(User user);
}
