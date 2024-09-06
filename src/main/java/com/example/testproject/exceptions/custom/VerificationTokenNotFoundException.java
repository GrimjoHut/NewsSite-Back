package com.example.testproject.exceptions.custom;


import com.example.testproject.exceptions.GlobalAppException;

public class VerificationTokenNotFoundException extends GlobalAppException {
    public VerificationTokenNotFoundException() {
        super(404, "Verification token not found");
    }
}
