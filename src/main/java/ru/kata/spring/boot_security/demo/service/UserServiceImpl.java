package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {


    private final UserDAO userDAOrepository;
    private final RoleDao roleDaorepository;
    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    @Lazy
    public UserServiceImpl(UserDAO userDAOrepository, RoleDao roleDaorepository, PasswordEncoder passwordEncoder) {
        this.userDAOrepository = userDAOrepository;
        this.roleDaorepository = roleDaorepository;
        this.passwordEncoder = passwordEncoder;
//        this.entityManager = entityManager;
    }

    @Override//
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAOrepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPassword(), user.getRoles());
    }


    @Override//
    public List<User> getAllUsers() {
        return userDAOrepository.findAll();
    }

    @Override//
    public User getUserById(long id) {
        User user = userDAOrepository.getUserById(id);
        if (user != null) {
            return userDAOrepository.getUserById(id);
        } else throw new RuntimeException("User by id: " + id + " in DB not found");
    }

    @Override
    @Transactional
    public void deleteUserById(long id) {
        userDAOrepository.deleteUserById(id);
    }

    @Override
    public void deleteUserByName(String name) {

    }

    @Override
    @Transactional//
    public void updateUser(User user) {
//        userDAOrepository.updateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.merge(user);
        userDAOrepository.save(user);
    }

    @Override
    @Transactional//
    public void addUser(User user) {
//        userDAOrepository.addUser(user);
        User userByIdFromDB = userDAOrepository.getUserById(user.getId());

        if (userByIdFromDB == null) {
            createRolesIfNotExist();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDAOrepository.saveAndFlush(user);
        } else throw new RuntimeException("User by id: " + user.getId() + " in DB already exist");
    }

    @Override//
    public User getUserByName(String username) {
        return userDAOrepository.findByName(username);
    }

    @Override
    @Transactional//
    public void createRolesIfNotExist() {
        if (roleDaorepository.findById(1L).isEmpty()) {
            roleDaorepository.save(new Role(1L, "ROLE_ADMIN"));
        }
        if (roleDaorepository.findById(2L).isEmpty()) {
            roleDaorepository.save(new Role(2L, "ROLE_USER"));
        }
    }

    @Override///
    @Transactional(readOnly = true)
    public List<Role> getListOfRoles() {
        return roleDaorepository.findAll();
    }

    @Override
    public Set<GrantedAuthority> getAuthorities(User user) {

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }
}
