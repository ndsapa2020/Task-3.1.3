package com.jm.springBootstrap.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Component("roleBean")
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "role")
    private String role;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<User> users;

    @Transient
    private static Set<Role> rolesSet = new HashSet<>();

    public Role() {
    }

    public Role(long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public static void setRolesSet(){
        Role roleUser = new Role(1L, "ROLE_USER");
        Role roleAdmin = new Role(2L, "ROLE_ADMIN");
        rolesSet.add(roleUser);
        rolesSet.add(roleAdmin);
    }

    public static Set<Role> getRolesSet() {
        return rolesSet;
    }

    @Override
    public String toString() {
        return "Role {" +
                "id=" + id +
                ", role=" + role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}

