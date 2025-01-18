package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.Repository.RoleRepository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;


@Controller
public class RegistrationController {


    private final UserService userService;
    private final RoleRepository roleRepository;


    @Autowired
    public RegistrationController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;

    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute("user") User user, @RequestParam(value = "roles", required = false) List<Role> roles) {
        userService.saveUser(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/createUser")
    public String createUserForm(Model model) {
        List<Role> roleAlls = roleRepository.findAll();
        model.addAttribute("user", new User());
        model.addAttribute("role_all", roleAlls);
        return "createUser";
    }

    @GetMapping("/user")
    public String showUserInfo(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }
}
