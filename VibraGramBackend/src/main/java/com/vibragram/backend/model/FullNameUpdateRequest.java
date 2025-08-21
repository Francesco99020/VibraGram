package com.vibragram.backend.model;

import jakarta.validation.constraints.Size;

public class FullNameUpdateRequest {
    @Size(max = 100, message = "Name cannot be more than 100 characters long.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
