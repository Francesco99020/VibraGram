package com.vibragram.backend.model;

import jakarta.validation.constraints.Size;

public class BioUpdateRequest {
    @Size(max = 150, message = "Bio cannot be more than 150 characters long.")
    private String bio;
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}

