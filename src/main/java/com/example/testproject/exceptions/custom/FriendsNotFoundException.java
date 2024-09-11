package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class FriendsNotFoundException extends GlobalAppException {
    public FriendsNotFoundException() {
        super(404, "Friends not found");
    }
}
