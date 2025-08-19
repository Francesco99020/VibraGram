package com.vibragram.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AppUser extends User {
    private int userId;
    private static final String AUTHORITY_PREFIX = "ROLE_";

    @NotBlank(message = "Email is required for all users.")
    @Email(message = "User email must be a valid email address.")
    private String email;

    @NotNull(message = "Created at date is required.")
    @PastOrPresent(message = "Created at date cannot be in the future.")
    private LocalDateTime createdAt;

    private boolean isAdmin;

    public AppUser(int userId, String username, String email, String hashedPassword,
                   LocalDateTime createdAt, boolean isAdminFromDb, boolean enabled) {
        super(username,
                hashedPassword,
                enabled,
                true,
                true,
                true,
                generateAuthoritiesFromIsAdmin(isAdminFromDb));

        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
        this.isAdmin = isAdminFromDb;
    }

    public AppUser() {
        super("default_username", "",
                Collections.singletonList(new SimpleGrantedAuthority(AUTHORITY_PREFIX + "USER")));
        this.userId = 0;
        this.email = ""; // Default email
        this.createdAt = LocalDateTime.now(); // Default createdAt
        this.isAdmin = false; // Default to not admin
    }

    public AppUser(int userId, String username, String email,
                   String hashedPassword, LocalDateTime createdAt,
                   boolean disabled, List<String> roles) {
        super(username, hashedPassword, !disabled, // 'enabled' is the opposite of 'disabled'
                true, true, true,
                convertRolesToAuthorities(roles));
        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
        this.isAdmin = roles != null && roles.stream()
                .map(String::toUpperCase)
                .anyMatch(role -> role.equals("ADMIN"));
    }

    public AppUser(int userId, String username, String email, String hashedPassword,
                   LocalDateTime createdAt, boolean enabled,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username, hashedPassword, enabled, true,
                true, true, authorities);
        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
        this.isAdmin = authorities != null && authorities.stream()
                .anyMatch(auth -> auth.getAuthority()
                        .equals(AUTHORITY_PREFIX + "ADMIN"));
    }



    private static List<GrantedAuthority> generateAuthoritiesFromIsAdmin(boolean isAdminFlag) {
        List<String> roles = new ArrayList<>();
        roles.add("USER"); // All users are at least "USER"
        if (isAdminFlag) {
            roles.add("ADMIN");
        }
        return convertRolesToAuthorities(roles);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AppUser appUser = (AppUser) o;
        return userId == appUser.userId &&
                isAdmin == appUser.isAdmin &&
                Objects.equals(email, appUser.email) &&
                Objects.equals(createdAt, appUser.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, email, createdAt, isAdmin);
    }

    public static List<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles == null) {
            return authorities;
        }
        for (String role : roles) {
            Assert.isTrue(role != null && !role.startsWith(AUTHORITY_PREFIX),
                    () -> String.format("Role '%s' cannot start with '%s' (it is automatically added) or be null",
                            role, AUTHORITY_PREFIX));
            authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + role.toUpperCase())); // Ensure roles are uppercase for consistency
        }
        return authorities;
    }

    public static List<String> convertAuthoritiesToRoles(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return Collections.emptyList();
        }
        return authorities.stream()
                .filter(Objects::nonNull)
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(a -> a.startsWith(AUTHORITY_PREFIX))
                .map(a -> a.substring(AUTHORITY_PREFIX.length()))
                .collect(Collectors.toList());
    }
}