package com.example.demo.exception.model;

public class ResourceAlreadyPresent extends RuntimeException{
    public ResourceAlreadyPresent(String message) {
        super(message);
    }
}
