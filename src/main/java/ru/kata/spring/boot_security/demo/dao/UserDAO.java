package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

//@Repository
public interface UserDAO extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByName(String username);


//    public List<User> getAllUsers();
    public User getUserById(long id);
    public void deleteUserById(long id);
    public void deleteUserByName(String username);
//    public void updateUser(User user);
//    public void addUser(User user);
}
