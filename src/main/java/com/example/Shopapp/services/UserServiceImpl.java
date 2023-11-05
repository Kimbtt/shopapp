package com.example.Shopapp.services;

import com.example.Shopapp.dtos.UserDto;
import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.Role;
import com.example.Shopapp.models.User;
import com.example.Shopapp.repositories.RoleRepository;
import com.example.Shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService{
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Override
    public User createUser(UserDto userDto) throws DataNotFoundException {
        String phoneNumber = userDto.getPhoneNumber();
        // Kiểm tra xem số điện thoại đã tồn tại hay chưa
        boolean existPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);
        if (existPhoneNumber) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        // Convert from userDtp => user
        User newUser = User.builder()
                .fullName(userDto.getFullName())
                .phoneNumber(userDto.getPhoneNumber())
                .password(userDto.getPassword())
                .dateOfBirth(userDto.getDateOfBirth())
                .facebookAccountId(userDto.getFacebookAcountId())
                .googleAccountId(userDto.getGoogleAcountId())
                .build();
        Role role = roleRepository.findById(userDto.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("Role not found"));

        newUser.setRoleId(role);

        // Kiểm tra nếu có accountId => không yêu cầu password
        if (userDto.getGoogleAcountId() == 0 & userDto.getFacebookAcountId() == 0){
            String password = userDto.getPassword();
        }

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {


        return null;
    }
}
