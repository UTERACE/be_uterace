package com.be_uterace.service.impl;

import com.be_uterace.entity.User;
import com.be_uterace.payload.request.ChangePasswordDto;
import com.be_uterace.payload.request.UpdateDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.payload.response.UserResponse;
import com.be_uterace.repository.AreaRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.FileService;
import com.be_uterace.service.UserService;
import com.be_uterace.utils.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.be_uterace.mapper.UserMapper.convertFromUserToUserResponse;
import static com.be_uterace.utils.DateConverter.convertStringToDate;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AreaRepository areaRepository;
    private ModelMapper modelMapper;
    private FileService fileService;
    @Value("${path.image}")
    private String path;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AreaRepository areaRepository, ModelMapper modelMapper, FileService fileService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.areaRepository = areaRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
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
                user.setFirstName(!Objects.equals(updateDto.getFirstname(), "") ? updateDto.getFirstname() : user.getFirstName());
                user.setLastName(!Objects.equals(updateDto.getLastname(), "") ? updateDto.getLastname() : user.getLastName());
                user.setEmail(!Objects.equals(updateDto.getEmail(), "") ? updateDto.getEmail() : user.getEmail());
                user.setTelNum(!Objects.equals(updateDto.getTelNumber(), "") ? updateDto.getTelNumber() : user.getTelNum());
                user.setDateOfBirth(!Objects.equals(updateDto.getBirthday(), "") ? convertStringToDate(updateDto.getBirthday()) : user.getDateOfBirth());
                user.setGender(!Objects.equals(updateDto.getGender(), "") ? updateDto.getGender() : user.getGender());
                user.setHomeNumber(!Objects.equals(updateDto.getAddress(), "") ? updateDto.getAddress() : user.getHomeNumber());
                user.setArea(!Objects.equals(updateDto.getProvince(), "") && !Objects.equals(updateDto.getDistrict(), "") && !Objects.equals(updateDto.getWard(), "") ? areaRepository.findArea(updateDto.getProvince(),
                        updateDto.getDistrict(),
                        updateDto.getWard()) : user.getArea());
//                user.setArea(areaRepository.findArea(updateDto.getProvince(),
//                        updateDto.getDistrict(),
//                        updateDto.getWard()));
                if (user.getTypeAccount().equals("default")){
                    if (!user.getAvatarPath().equals(updateDto.getImage()) && !Objects.equals(updateDto.getImage(), "")){
                        if (Objects.equals(user.getAvatarPath(), "")){
                            user.setAvatarPath(path+ fileService.saveImage(updateDto.getImage()));
                        }else if (fileService.deleteImage(user.getAvatarPath())){
                            System.out.println("Delete image success");
                            user.setAvatarPath(path+ fileService.saveImage(updateDto.getImage()));
                        }
                    }
                }
                userRepository.save(user);
                return ResponseObject.builder()
                        .status(200)
                        .message("Cập nhật thông tin ngừoi dùng \n" +
                                " thành công!")
                        .build();
            }
        }
        return ResponseObject.builder()
                .status(400)
                .message("Cập nhật thông tin ngừoi dùng \n" +
                        " thất bại!")
                .build();
    }
}
