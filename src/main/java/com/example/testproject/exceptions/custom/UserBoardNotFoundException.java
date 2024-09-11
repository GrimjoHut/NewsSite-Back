package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class UserBoardNotFoundException extends GlobalAppException {
    public UserBoardNotFoundException() {
        super(404, "UserBoard not found");
    }
}
