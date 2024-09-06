package com.example.testproject.models.enums;

public enum RoleEnum {
    ROLE_USER, ROLE_MODER, ROLE_ADMIN, ROLE_GOD;

    public boolean canAssign(RoleEnum role) {
        switch (this) {
            case ROLE_GOD:
                return true;
            case ROLE_ADMIN:
                return role == ROLE_MODER;
            case ROLE_MODER:
                return false;
            case ROLE_USER:
            default:
                return false;
        }
    }

    public boolean canRevoke(RoleEnum role) {
        switch (this) {
            case ROLE_GOD:
                return true;
            case ROLE_ADMIN:
                return role == ROLE_MODER;
            case ROLE_MODER:
                return false;
            case ROLE_USER:
            default:
                return false;
        }
    }
}
