package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class UserCreationConflict extends GlobalAppException {
    public UserCreationConflict() {
        super(409, "User already exists");
    }
}
