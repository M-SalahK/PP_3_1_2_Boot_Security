package ru.kata.spring.boot_security.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/index")
    public String getAllUsers(Model model) {
        model.addAttribute("listUsers", userService.findAllUsers());
        return "index";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "roles", required = false) List<Long> rolesId,
                             @RequestParam(value = "id") Long id)
    {
        roleService.findRoleById(rolesId);
        user.setRoles(roleService.findRoleById(rolesId));
        userService.updateUser(user, rolesId, id);
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
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("role_all", roleService.getRoles());
        return "update";
    }

    @GetMapping("/admin")
    public String showAdminForm(Authentication authentication, Model model) {
        model.addAttribute("user", userService.getPrincipalUser(authentication));
        return "admin";

    }
}
