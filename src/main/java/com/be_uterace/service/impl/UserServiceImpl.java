package com.be_uterace.service.impl;

import com.be_uterace.entity.User;
import com.be_uterace.payload.request.ChangePasswordDto;
import com.be_uterace.payload.request.UpdateDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.UserResponse;
import com.be_uterace.repository.AreaRepository;
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
import static com.be_uterace.utils.DateConverter.convertStringToDate;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AreaRepository areaRepository;
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AreaRepository areaRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.areaRepository = areaRepository;
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

    @Override
    public ResponseObject updateUser(UpdateDto updateDto, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setFirstName(updateDto.getFirstname());
                user.setLastName(updateDto.getLastname());
                user.setEmail(updateDto.getEmail());
                user.setTelNum(updateDto.getTelNumber());
                user.setDateOfBirth(convertStringToDate(updateDto.getBirthday()));
                user.setGender(updateDto.getGender());
                user.setHomeNumber(updateDto.getAddress());
                user.setArea(areaRepository.findArea(updateDto.getProvince(),
                        updateDto.getDistrict(),
                        updateDto.getWard()));
                user.setAvatarPath(updateDto.getImage());
                userRepository.save(user);
                return ResponseObject.builder()
                        .status(200)
                        .message("Cập nhật thông tin ngừoi dùng \n" +
                                " thành công!")
                        .build();
            }
        }
        return null;
    }
}
