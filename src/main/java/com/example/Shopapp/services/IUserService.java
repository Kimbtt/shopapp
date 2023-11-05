package com.example.Shopapp.services;

import com.example.Shopapp.dtos.UserDto;
import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.User;

public interface IUserService {
    User createUser(UserDto userDto) throws DataNotFoundException;
    String login(String phoneNumber, String password);
}
