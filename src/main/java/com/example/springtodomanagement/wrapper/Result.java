package com.example.springtodomanagement.wrapper;

import java.util.List;

public class Result<T> extends BaseResult {
    public T data;

    public Result(T data) {
        this.data = data;
        this.isSuccess=true;
    }

    public Result(List<Error> errors) {
        this.errors = errors;
        this.isSuccess = false;
    }

    public Result(Error error) {
        this.errors.add(error);
        this.isSuccess = false;
    }

}
