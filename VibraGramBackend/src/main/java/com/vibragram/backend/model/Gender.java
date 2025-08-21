package com.vibragram.backend.model;

public enum Gender{
    MALE("male"),
    FEMALE("female");

    private final String dbValue;

    Gender(String dbValue){
        this.dbValue = dbValue;
    }

    public String getDbValue(){
        return dbValue;
    }

    public static Gender fromDbValue(String value) {
        for (Gender g : values()) {
            if (g.dbValue.equalsIgnoreCase(value)) return g;
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
