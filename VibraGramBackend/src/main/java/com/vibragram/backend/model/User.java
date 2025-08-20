package com.vibragram.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private long userId;

    @NotBlank(message = "Username is required for all users")
    @NotNull (message = "Username must not be null.")
    private String username;

    @NotBlank(message = "Email is required for all users")
    @Email(message = "User email must be valid email address.")
    private String email;

    @NotBlank(message = "Full name is required for all users")
    @NotNull(message = "Full name must not be null")
    private String fullName;

    private String bio;

    private String profilePic;

    private boolean isAdmin;

    @NotNull(message = "Created at date is required.")
    @PastOrPresent(message = "Created at date cannot be in the future.")
    private LocalDateTime createdAt;

    @NotNull(message = "Updated at date is required.")
    @PastOrPresent(message = "Updated at date cannot be in the future.")
    private LocalDateTime updatedAt;

    public User(long userId, String username, String email, String fullName, String bio,
                String profilePic, boolean isAdmin, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePic = profilePic;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(){
        this.userId = -1;
        this.username = "";
        this.email = "";
        this.fullName = "";
        this.bio = "";
        this.profilePic = "";
        this.isAdmin = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return userId == user.userId && isAdmin == user.isAdmin && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(fullName, user.fullName) && Objects.equals(bio, user.bio) && Objects.equals(profilePic, user.profilePic) && Objects.equals(createdAt, user.createdAt) && Objects.equals(updatedAt, user.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email, fullName, bio, profilePic, isAdmin, createdAt, updatedAt);
    }
}
