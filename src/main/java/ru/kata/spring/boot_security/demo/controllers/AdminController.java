package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
public class AdminController {

    final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String getAllUsers(Model model) {
        model.addAttribute("listUsers", userService.findAllUsers());
        return "index";
    }

    @GetMapping("/update/")
    public String updateUser(@RequestParam("id") long id, Model model) {
        User user1 = userService.findUserById(id);
        userService.updateUser(user1);
        model.addAttribute("update", userService.findUserById(id));
        return "redirect:index";
    }

    @PostMapping("/createAdmin")
    public String createAdmin(User newAdmin) {
        userService.saveAdmin(newAdmin);
        return "redirect:index";
    }

    @GetMapping("/admin")
    public String getAdmin(Model model) {
        model.addAttribute("admin", new User());
        return "index";
    }

    @PostMapping("/index/{id}")
    public String deleteAdmin(@PathVariable long id) {
        userService.deleteUser(id);
        return "redirect:index";
    }
    @GetMapping("/index/{id}")
    public String delete(@PathVariable long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "index";
    }

}
