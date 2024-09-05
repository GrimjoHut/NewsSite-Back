package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class UserNotFoundException extends GlobalAppException {
    public UserNotFoundException() {
        super(404, "User not found");
    }
}
