package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class CommentaryNotFoundException extends GlobalAppException {
    public CommentaryNotFoundException() {
        super(404, "Commentary not found");
    }
}
