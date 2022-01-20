package com.r5n.gradetrackerapi.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @NotBlank
    private String id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Email
    @Column(name = "email")
    private String email;

    public User() {
    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
