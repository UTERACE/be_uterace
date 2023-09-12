package com.be_uterace.service.impl;

import com.be_uterace.entity.User;
import com.be_uterace.payload.request.ChangePasswordDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.UserResponse;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.UserService;
import com.be_uterace.utils.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.be_uterace.mapper.UserMapper.convertFromUserToUserResponse;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserResponse getUserInfo(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> user = userRepository.findByUsername(username);
            User user1 = user.get();
            return convertFromUserToUserResponse(user1);
        }
        return null;
    }

    @Override
    public ResponseObject changePassword(ChangePasswordDto changePasswordDto, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (passwordEncoder.matches(changePasswordDto.getOld_password(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(changePasswordDto.getNew_password()));
                    userRepository.save(user);
                    return ResponseObject.builder()
                            .status(200)
                            .message("Update password successfully!")
                            .build();
                }
            }
        }
        return null;
    }
}
