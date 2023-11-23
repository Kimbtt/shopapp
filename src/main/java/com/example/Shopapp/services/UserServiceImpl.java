package com.example.Shopapp.services;

import com.example.Shopapp.models.dtos.UserDto;
import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.entity.Role;
import com.example.Shopapp.models.entity.User;
import com.example.Shopapp.repositories.RoleRepository;
import com.example.Shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
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

        newUser.setRole(role);

        // Kiểm tra nếu có accountId => không yêu cầu password
        if (userDto.getGoogleAcountId() == 0 & userDto.getFacebookAcountId() == 0){
            String password = userDto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }

        return userRepository.save(newUser);
    }

    @Override
    public User login(String phoneNumber, String password) throws DataNotFoundException {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid phonenumber / password");
        }

        return optionalUser.get(); // muốn trả về jwt token?
    }
}
