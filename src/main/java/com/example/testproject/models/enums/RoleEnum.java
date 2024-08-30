package com.example.testproject.models.enums;

public enum RoleEnum {
    USER, MODER, ADMIN, GOD;

    public boolean canAssign(RoleEnum role) {
        switch (this) {
            case GOD:
                return true;
            case ADMIN:
                return role == MODER;
            case MODER:
                return false;
            case USER:
            default:
                return false;
        }
    }

    public boolean canRevoke(RoleEnum role) {
        switch (this) {
            case GOD:
                return true;
            case ADMIN:
                return role == MODER;
            case MODER:
                return false;
            case USER:
            default:
                return false;
        }
    }
}
