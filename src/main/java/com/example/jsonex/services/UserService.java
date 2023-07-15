package com.example.jsonex.services;

import com.example.jsonex.models.dtos.UserSoldDto;
import com.example.jsonex.models.entities.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void seedUsers() throws IOException;

    User findRandomUser();

    List<UserSoldDto> findAllUsersWithMoreThanOneSoldProducts();
}
