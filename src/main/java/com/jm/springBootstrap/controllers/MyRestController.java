package com.jm.springBootstrap.controllers;

import com.jm.springBootstrap.model.Role;
import com.jm.springBootstrap.model.User;
import com.jm.springBootstrap.service.UserService;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class MyRestController {

    final UserService userService;

    public MyRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users", produces = "application/json")
    public ResponseEntity<List<User>> readAll() {
        List<User> users = userService.findAll();
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getOne(@PathVariable("id") Long userId) {
        final User user = userService.findById(userId);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/users", produces = "application/json")
    public ResponseEntity<User> saveCustomer(@RequestBody JSONObject json) throws JSONException {
        String login = json.getString("login");
        String lastName = json.getString("lastName");
        String password = json.getString("password");
        int age = Integer.parseInt(json.getString("age"));
        String email = json.getString("email");

        Set<Role> rolesToAdd = new HashSet<>();
        if (json.getString("isUser").equals("on")) {
            rolesToAdd.add(new Role(1L, "ROLE_USER"));
        }
        if (json.getString("isAdmin").equals("on")) {
            rolesToAdd.add(new Role(2L, "ROLE_ADMIN"));
        }

        User user = new User();
        user.setLogin(login);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setAge(age);
        user.setEmail(email);
        user.setRoles(rolesToAdd);

        userService.saveNewUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(ModelMap model,
                                           @PathVariable Long id,
                                           @RequestBody JSONObject json) throws JSONException {
        User user = userService.findById(id);

        String login = json.getString("login");
        String lastName = json.getString("lastName");
        String password = json.getString("password");
        int age = Integer.parseInt(json.getString("age"));
        String email = json.getString("email");

        Set<Role> rolesToAdd = new HashSet<>();
        if (json.getString("isUser").equals("on")) {
            rolesToAdd.add(new Role(1L, "ROLE_USER"));
        }
        if (json.getString("isAdmin").equals("on")) {
            rolesToAdd.add(new Role(2L, "ROLE_ADMIN"));
        }
        user.setLogin(login);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAge(age);
        user.setPassword(password);
        // user.setRoles(rolesToAdd);
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
