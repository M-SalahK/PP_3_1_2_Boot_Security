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
public class AdminController {

    final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/index")
    public String getAllUsers(Model model) {
        model.addAttribute("listUsers", userService.findAllUsers());
        return "index";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam(value = "roles", required = false) List<Long> rolesId, @RequestParam(value = "id") Long id) {
        List<Role> roles = roleRepository.findAllById(rolesId);
        user.setRoles(roles);
        userService.updateUser(user, roles, id);
        return "redirect:/index";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        userService.deleteUser(id);
        return "redirect:index";
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam("id") Long id, Model model, @ModelAttribute("user") User user) {
        user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("role_all", roleRepository.findAll());
        return "update";
    }

    @GetMapping("/admin")
    public String showAdminForm(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "admin";

    }
}
