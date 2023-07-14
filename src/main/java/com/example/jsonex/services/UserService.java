package com.example.jsonex.services;

import com.example.jsonex.models.entities.User;

import java.io.IOException;

public interface UserService {
    void seedUsers() throws IOException;

    User findRandomUser();
}
