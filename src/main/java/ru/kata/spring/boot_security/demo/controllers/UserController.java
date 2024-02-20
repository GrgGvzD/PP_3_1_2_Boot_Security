package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.User;

import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;
    private final RoleDao roleDAOrepository;

    @Autowired
    public UserController(UserService userService, RoleDao roleDAOrepository) {
        this.userService = userService;
        this.roleDAOrepository = roleDAOrepository;
    }

    @GetMapping("")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
//        return "index";
    }
    @GetMapping("/showUserInfo")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println("!!!!!!!!!!!!!!!!" + user);
        return "user";
    }

//
//    @GetMapping("/get_user")
//    public String getUser(@RequestParam(value = "id", required = false) int id, Model model) {
//        model.addAttribute("user", userService.getUserById(id));
//        return "/user";
//    }
//
//    @GetMapping("/new")
//    public String getNewUserForm(@ModelAttribute("user") User user) {
//        return "/new_user";
//    }
//
//    @PostMapping("/new")
//    public String creatNewUser(@ModelAttribute User user) {
//        userService.addUser(user);
//        return "redirect:users";
//    }
//
//
//    @GetMapping("/edit")
//    public ModelAndView editUserForm(@RequestParam Integer id) {
//        ModelAndView mav = new ModelAndView("/edit_user");
//        User user = (User) userService.getUserById(id);
//        System.out.println("get mapping edit "+user);
//        mav.addObject("user", user);
//        return mav;
//    }
//
//    @PostMapping("/edit")
//    public String editUser(@ModelAttribute("user") User user) {
//        userService.updateUser(user);
//        return "redirect:users";
//    }
//
//    @PostMapping("/del_user")
//    public String deleteUser(@RequestParam("id") Integer id) {
//        userService.deleteUserById(id);
//        return "redirect:users";
//    }
}
