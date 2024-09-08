package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;

public class ReportNotFoundException extends GlobalAppException {
    public ReportNotFoundException() {
        super(404, "Report not found");
    }
}
