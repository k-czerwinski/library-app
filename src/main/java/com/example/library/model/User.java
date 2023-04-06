package com.example.library.model;

import com.example.library.model.enums.UserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated
    @Column(name = "role")
    private UserRole userRole;
//    @Size(min=2, max = 20)
    private String name;
//    @Size(min=2, max = 30)
    private String surname;

//    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
//    @NotEmpty(message = "Email cannot be empty")
    private String email;

//    @ValidPassword
    private String password;

    public User() {
    }

    public User(UserRole userRole, String name, String surname, String email, String password) {
        this.userRole = userRole;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}