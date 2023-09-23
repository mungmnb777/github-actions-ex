package com.runwithme.runwithme.domain.user.entity;

public enum Role {
    TEMP_USER(10, "ROLE_TEMP_USER"), USER(20, "ROLE_USER");

    private final int status;
    private final String value;

    Role(int status, String value) {
        this.status = status;
        this.value = value;
    }

    public int getStatus() {
        return status;
    }

    public String getValue() {
        return value;
    }
}
