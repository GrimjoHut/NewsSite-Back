package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class PostNotFoundException extends GlobalAppException {
    public PostNotFoundException() {
        super(404, "Post not found");
    }
}
