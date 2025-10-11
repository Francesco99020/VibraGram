package com.vibragram.backend.model.Request;

import com.vibragram.backend.model.Gender;

public class GenderUpdateRequest {
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    private Gender gender;
}
