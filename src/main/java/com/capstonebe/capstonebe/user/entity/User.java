package com.capstonebe.capstonebe.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    private String email;

    private String password;

    private String name;

    private String phone;

    private boolean isActive;

    private int warnCount;

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void update(String password, String name, String phone) {
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public void addWarning() {
        this.warnCount++;
    }
}
