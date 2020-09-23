package com.jm.springBootstrap.controllers;

import com.jm.springBootstrap.model.Role;
import com.jm.springBootstrap.model.User;
import com.jm.springBootstrap.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {

    private static boolean isInit = false; // change to FALSE to STOP new Init
    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String indexPage() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginByEmail(@RequestParam(value = "error", required = false) String error,
                                  @RequestParam(value = "logout", required = false) String logout,
                                  Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "my_login";
    }

    @GetMapping("hello")
    public String printWelcome(Principal principal) {
        if (isInit) {
            insertDataToDatabase(); // correct it later
            isInit = false;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userRoles = auth.getAuthorities().toString();
        if (userRoles.contains("ROLE_ADMIN")) {
            return "redirect:/admin";
        }
        return "redirect:/user";
    }

    @GetMapping("/admin")
    public String startAdmin(ModelMap modelMap, Principal principal) {
        List<User> userList = userService.findAll();
        Set<Role> rolesList = Role.getRolesSet();
        modelMap.addAttribute("usersList", userList);
        modelMap.addAttribute("roles", rolesList);
        return "/index";
    }

    @GetMapping("user")
    public ModelAndView index(Principal principal, ModelAndView mav) {
        User user = userService.getUserByEmail(principal.getName());
        mav.addObject("user", user);
        mav.setViewName("/user");
        return mav;
    }

    private void insertDataToDatabase() {
        System.out.println("\nInserting data ....");
        Role roleUser = new Role(1L, "ROLE_USER");
        Role roleAdmin = new Role(2L, "ROLE_ADMIN");

        Set<Role> bothSet = new HashSet<Role>();
        bothSet.add(roleUser);
        bothSet.add(roleAdmin);
        Set<Role> admSet = new HashSet<>();
        admSet.add(roleAdmin);
        Set<Role> userSet = new HashSet<>();
        userSet.add(roleUser);

        User marlo = new User("admin", "admin", "admin@adm.in", "admin", 1955, admSet);
        User john = new User("user", "userin", "user@user.com", "user", 1960, userSet);
        User jackson = new User("boss", "superboss", "boss@boss.ru", "boss", 1965, bothSet);
        User jagger = new User("Ben", "Johnson", "bj@yahoo.com", "12345", 1956, admSet);
        User tiger = new User("Laura", "Crawford", "lora@mail.ar", "111", 1970, bothSet);

        userService.saveUser(marlo);
        userService.saveUser(john);
        userService.saveUser(jackson);
        userService.saveUser(jagger);
        userService.saveUser(tiger);
    }
}
