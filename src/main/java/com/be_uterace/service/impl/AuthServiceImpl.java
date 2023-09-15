package com.be_uterace.service.impl;

import com.be_uterace.entity.Role;
import com.be_uterace.entity.User;
import com.be_uterace.payload.request.LoginDto;
import com.be_uterace.payload.request.RegisterDto;
import com.be_uterace.payload.response.LoginResponse;
import com.be_uterace.payload.response.RefreshTokenResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.AreaRepository;
import com.be_uterace.repository.RoleRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.security.JwtTokenProvider;
import com.be_uterace.service.AuthService;
import com.be_uterace.utils.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.be_uterace.mapper.UserMapper.convertFromRegisterDtoToUser;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AreaRepository areaRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           AreaRepository areaRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.areaRepository = areaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public LoginResponse login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        Optional<User> userOptional = userRepository.findByUsername(loginDto.getUsername());
        User user = userOptional.get();
        Set<Role> roles = user.getRoles();
        List<Map<String, Object>> roleInfoList = roles.stream()
                .map(role -> {
                    Map<String, Object> roleInfo = new HashMap<>();
                    roleInfo.put("roleId", role.getRoleId());
                    roleInfo.put("roleName", role.getRoleName());
                    return roleInfo;
                })
                .sorted(Comparator.comparingLong(roleInfo -> (Long) roleInfo.get("roleId"))) // Sắp xếp theo roleId
                .collect(Collectors.toList());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .image(user.getAvatarPath())
                .roles(roleInfoList)
                .build();
    }

    @Override
    public ResponseObject register(RegisterDto registerDto) {
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        User user = convertFromRegisterDtoToUser(registerDto);
        user.setArea(areaRepository.findArea(registerDto.getProvince(),
                registerDto.getDistrict(),
                registerDto.getWard()));
        userRepository.save(user);
        return ResponseObject.builder().status(201)
                .message(Constant.SUCCESS_REGISTER)
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        if(StringUtils.hasText(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {

            // get username from token
            String username = jwtTokenProvider.getUsername(refreshToken);
            Optional<User> userOptional = userRepository.findByUsername(username);
            User user = userOptional.get();

            String access_Token = jwtTokenProvider.generateAccessToken(user.getUsername());
            String refresh_Token = jwtTokenProvider.generateRefreshToken(user.getUsername());

            return RefreshTokenResponse.builder()
                    .accessToken(access_Token)
                    .refreshToken(refresh_Token)
                    .build();
        }
        return null;
    }

    @Override
    public ResponseObject resetPassword(RegisterDto registerDto) {
        return null;
    }
}
