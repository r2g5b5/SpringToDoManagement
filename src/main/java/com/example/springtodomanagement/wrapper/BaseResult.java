package com.example.springtodomanagement.wrapper;

import java.util.ArrayList;
import java.util.List;


public class BaseResult {
    public boolean isSuccess;
    public List<Error> errors=new ArrayList<>();


    public BaseResult() {
        this.isSuccess = true;
    }

    public BaseResult(List<Error> errors) {
        this.errors = errors;
        this.isSuccess = false;
    }

    public BaseResult(Error error) {
        this.errors.add(error);
        this.isSuccess = false;
    }
}

