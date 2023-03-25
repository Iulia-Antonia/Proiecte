package com.example.SocialNettwork.Domain.validators;

public interface Validator<T> {
    void validate(T entity)throws ValidationException;
}
