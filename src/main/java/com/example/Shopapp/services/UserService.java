package com.example.Shopapp.services;

import com.example.Shopapp.models.dtos.UserDto;
import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.entity.User;

public interface UserService {
    User createUser(UserDto userDto) throws DataNotFoundException;
    String login(String phoneNumber, String password) throws Exception;
}
