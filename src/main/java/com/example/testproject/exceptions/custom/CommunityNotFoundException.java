package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class CommunityNotFoundException extends GlobalAppException {

    public CommunityNotFoundException() {
        super(404, "Community not found");
    }
}
