package com.example.testproject.exceptions.custom;

import com.example.testproject.exceptions.GlobalAppException;
import org.springframework.http.HttpStatus;

public class LikeSearchInvalidParametrs extends GlobalAppException {
    public LikeSearchInvalidParametrs() {
        super(400, "Either postId or commentId should be provided");
    }
}
