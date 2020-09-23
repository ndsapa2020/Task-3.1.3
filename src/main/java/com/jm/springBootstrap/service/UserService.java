package com.jm.springBootstrap.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jm.springBootstrap.model.Role;
import com.jm.springBootstrap.model.User;
import com.jm.springBootstrap.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow();
    }

    public User getUserByUsername(String login) {
        return userRepo.getUserByUsername(login);
    }

    public User getUserByEmail(String email) {
        return userRepo.getUserByEmail(email);
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    @JsonIgnore
    public List<User> findAll() {
        return userRepo.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepo.getUserByEmail(user.getEmail());
        if (userFromDB != null) {
            return false;
        }
        if (user.getRoles().size() == 0) {
            user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        }
        userRepo.save(user);
        return true;
    }

    public User saveNewUser(User user) {
        User userFromDB = userRepo.getUserByEmail(user.getEmail());
        if (userFromDB != null) {
            System.out.println("NS Error:  We have same user by Email!");
            return null;
        }
        if (user.getRoles().size() == 0) {
            user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        }

        return userRepo.save(user);
    }

    public boolean updateUser(User user) {


        if (user.getRoles().size() == 0) {
            user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        }
        userRepo.save(user);
        return true;
    }
}
