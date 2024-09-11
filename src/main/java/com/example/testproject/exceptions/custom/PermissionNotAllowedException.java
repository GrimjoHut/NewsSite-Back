package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class PermissionNotAllowedException extends GlobalAppException {
    public PermissionNotAllowedException() {
        super(401, "You don't have permission");
    }
}
