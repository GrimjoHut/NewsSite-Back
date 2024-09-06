package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class PermissionNotAllowed extends GlobalAppException {
    public PermissionNotAllowed() {
        super(401, "You don't have permission");
    }
}
