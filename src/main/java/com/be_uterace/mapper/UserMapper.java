package com.be_uterace.mapper;

import com.be_uterace.entity.User;
import com.be_uterace.payload.AreaDto;
import com.be_uterace.payload.request.RegisterDto;
import com.be_uterace.payload.response.UserResponse;
import com.be_uterace.repository.AreaRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.be_uterace.utils.DateConverter.convertDateToString;
import static com.be_uterace.utils.DateConverter.convertStringToDate;

@Component
@Transactional
public class UserMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public static User convertFromRegisterDtoToUser(RegisterDto registerDto) {
        return User.builder()
                .username(registerDto.getUsername())
                .password(registerDto.getPassword())
                .firstName(registerDto.getFirstname())
                .lastName(registerDto.getLastname())
                .email(registerDto.getEmail())
                .telNum(registerDto.getTelNumber())
                .dateOfBirth(convertStringToDate(registerDto.getBirthday()))
                .gender(registerDto.getGender())
                .homeNumber(registerDto.getAddress())
                .build();
    }

    public static UserResponse convertFromUserToUserResponse(User user) {
        return UserResponse.builder()
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .telNumber(user.getTelNum())
                .birthday(convertDateToString(user.getDateOfBirth()))
                .gender(user.getGender())
                .address(user.getHomeNumber())
                .province(user.getArea() != null ? user.getArea().getProvince() : null)
                .district(user.getArea() != null ? user.getArea().getDistrict() : null)
                .ward(user.getArea() != null ? user.getArea().getPrecinct() : null)
                .build();
    }
}
