package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Repository.RoleRepository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;


@Controller
public class AdminController {

    final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/index")
    public String getAllUsers(Model model, Authentication authentication) {
        boolean us = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("listUsers", userService.findAllUsers());
        model.addAttribute("us", us);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);
        return "index";
    }

    @PostMapping("/update")
    public String updateUser( @ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/index";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        userService.deleteUser(id);
        return "redirect:index";
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "update";
    }

    @GetMapping("/admin")
    public String showAdminForm(Model model, Authentication authentication) {
        return "admin";

    }
}
