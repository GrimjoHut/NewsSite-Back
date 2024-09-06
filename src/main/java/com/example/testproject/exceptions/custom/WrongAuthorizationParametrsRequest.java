package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class WrongAuthorizationParametrsRequest extends GlobalAppException {

    public WrongAuthorizationParametrsRequest() {
        super(400, "Wrong password or nickname");
    }
}
