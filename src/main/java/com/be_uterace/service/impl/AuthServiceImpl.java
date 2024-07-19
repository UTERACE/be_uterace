package com.be_uterace.service.impl;

import com.be_uterace.entity.Role;
import com.be_uterace.entity.User;
import com.be_uterace.exception.JWTException;
import com.be_uterace.exception.ResourceConflictException;
import com.be_uterace.payload.request.LoginDto;
import com.be_uterace.payload.request.RegisterDto;
import com.be_uterace.payload.request.ResetPasswordDto;
import com.be_uterace.payload.request.ThirdPartyDto;
import com.be_uterace.payload.response.*;
import com.be_uterace.repository.AreaRepository;
import com.be_uterace.repository.RoleRepository;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.security.JwtTokenProvider;
import com.be_uterace.service.AuthService;
import com.be_uterace.service.EmailService;
import com.be_uterace.utils.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static com.be_uterace.mapper.UserMapper.convertFromRegisterDtoToUser;
import static com.be_uterace.utils.RandomPasswordGenerator.generateRandomPassword;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AreaRepository areaRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private EmailService emailService;

    @Value("${google.recaptcha.key.secret}")
    String recaptchaSecret;
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           AreaRepository areaRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           UserDetailsService userDetailsService,
                           EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.areaRepository = areaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
    }

    @Override
    public LoginResponse login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername()).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Password is incorrect");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
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
                .id(user.getUserId())
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
    public ThirdPartyResponse thirdParty(ThirdPartyDto thirdPartyDto) {
        String urlLoginGoogle = "https://www.googleapis.com/oauth2/v2/userinfo";
        String urlLoginFacebook = "https://graph.facebook.com/me?fields=id,first_name,last_name,email,picture&access_token=";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + thirdPartyDto.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        RestTemplate restTemplate = new RestTemplate();
        if (thirdPartyDto.getType().equals("google")) {
            GoogleResponse response = restTemplate.exchange(urlLoginGoogle, HttpMethod.GET, entity, GoogleResponse.class).getBody();
            assert response != null;
            User user = userRepository.findByUsername(response.getEmail()).orElse(null);
            if (user == null) {
                return ThirdPartyResponse.builder()
                        .id(Long.valueOf(response.getId()))
                        .accessToken(null)
                        .refreshToken(null)
                        .isNewUser(true)
                        .firstname(response.getGiven_name())
                        .lastname(response.getFamily_name())
                        .email(response.getEmail())
                        .image(response.getPicture())
                        .roles(null)
                        .build();
            }else {
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
                return ThirdPartyResponse.builder()
                        .id(user.getUserId())
                        .accessToken(jwtTokenProvider.generateAccessToken(user.getUsername()))
                        .refreshToken(jwtTokenProvider.generateRefreshToken(user.getUsername()))
                        .isNewUser(false)
                        .firstname(user.getFirstName())
                        .lastname(user.getLastName())
                        .email(user.getEmail())
                        .image(user.getAvatarPath())
                        .roles(roleInfoList)
                        .build();
            }
        } else if (thirdPartyDto.getType().equals("facebook")) {
            FacebookResponse response = restTemplate.exchange(urlLoginFacebook + thirdPartyDto.getAccessToken(), HttpMethod.GET, entity, FacebookResponse.class).getBody();
            assert response != null;
            User user = userRepository.findByUsername(response.getId()+"-fb").orElse(null);
            if (user == null) {
                return ThirdPartyResponse.builder()
                        .id(Long.valueOf(response.getId()))
                        .accessToken(null)
                        .refreshToken(null)
                        .isNewUser(true)
                        .firstname(response.getFirst_name())
                        .lastname(response.getLast_name())
                        .email(response.getEmail())
                        .image(response.getPictureUrl())
                        .roles(null)
                        .build();
            }else {
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
                return ThirdPartyResponse.builder()
                        .id(user.getUserId())
                        .accessToken(jwtTokenProvider.generateAccessToken(user.getUsername()))
                        .refreshToken(jwtTokenProvider.generateRefreshToken(user.getUsername()))
                        .isNewUser(false)
                        .firstname(user.getFirstName())
                        .lastname(user.getLastName())
                        .email(user.getEmail())
                        .image(user.getAvatarPath())
                        .roles(roleInfoList)
                        .build();
            }
        }
        return null;
    }


    @Override
    public ResponseObject register(RegisterDto registerDto) {
        try {
            User userByUsername = userRepository.findByUsername(registerDto.getUsername()).orElse(null);
            if (userByUsername != null) {
                return ResponseObject.builder().status(409)
                        .message("Username is already in use")
                        .build();
            }
            User userByEmail = userRepository.findByEmail(registerDto.getEmail()).orElse(null);
            if (userByEmail != null) {
                return ResponseObject.builder().status(409)
                        .message("Email is already in use")
                        .build();
            }
            if (!isPasswordValid(registerDto.getPassword())) {
                return ResponseObject.builder().status(409)
                        .message("Password is not valid")
                        .build();
            }
            String url = "https://www.google.com/recaptcha/api/siteverify";
            String params = "?secret=" + recaptchaSecret + "&response=" + registerDto.getRecaptcha_token();
            RestTemplate restTemplate = new RestTemplate();
            RecaptchaResponse response = restTemplate.getForObject(url + params, RecaptchaResponse.class);
//            if (response.isSuccess() && response.getScore() >= 0.5) {
            if (!response.isSuccess()) {
                return ResponseObject.builder().status(409)
                        .message("Captcha is not valid")
                        .build();
            }
            if (response.getScore() < 0.5 && response.getScore() > 0) {
                return ResponseObject.builder().status(409)
                        .message("Are you a robot?")
                        .build();
            }
            // CAPTCHA xác thực thành công
            registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            User user = convertFromRegisterDtoToUser(registerDto);
//            user.setArea(areaRepository.findArea(registerDto.getProvince(),
//                    registerDto.getDistrict(),
//                    registerDto.getWard()));
            if (registerDto.getType_account().equals("google")) {
                user.setTypeAccount("google");
                user.setPassword(registerDto.getPassword());
                user.setUsername(registerDto.getEmail());
                user.setAvatarPath(registerDto.getImage());
            } else if (registerDto.getType_account().equals("facebook")) {
                user.setTypeAccount("facebook");
                user.setUsername(registerDto.getUsername() + "-fb");
                user.setPassword(registerDto.getPassword());
                user.setAvatarPath(registerDto.getImage());
            } else if (registerDto.getType_account().equals("default")) {
                user.setTypeAccount("default");
                user.setAvatarPath("");
            }
            user.setTotalDistance(0.0);
            user.setStatus("1");
            user.setHomeNumber(registerDto.getAddress());
            user.setName(registerDto.getLastname().trim() + " " + registerDto.getFirstname().trim());
            userRepository.save(user);
            return ResponseObject.builder().status(200)
                    .message(Constant.SUCCESS_REGISTER)
                    .build();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
            jwtTokenProvider.validateToken(refreshToken);

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

    @Override
    public ResponseObject resetPassword(ResetPasswordDto registerDto) {
        Boolean userBoolean = userRepository.existsByUsername(registerDto.getUsername());
        Boolean email = userRepository.existsByEmail(registerDto.getEmail());

        if (!userBoolean) {
            return ResponseObject.builder()
                    .status(409)
                    .message("Username not found")
                    .build();
        }

        if (!email) {
            return ResponseObject.builder()
                    .status(409)
                    .message("Email not found")
                    .build();
        }

        int passwordLength = 12;
        String randomPassword = generateRandomPassword(passwordLength);
        String randomPasswordEncoder = passwordEncoder.encode(randomPassword);

        Optional<User> userOptional = userRepository.findByUsername(registerDto.getUsername());
        User user = userOptional.get();
        user.setPassword(randomPasswordEncoder);


        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(registerDto.getEmail())
                .subject("Password Reset Request - UTE RACE")
                .msgBody("New password: " + randomPassword)
                .build();
        Boolean status
                = emailService.sendSimpleMail(emailDetails);

        if (!status) {
            return ResponseObject.builder()
                    .status(409)
                    .message("Reset password failed")
                    .build();
        }
        else{
            userRepository.save(user);
        }

        return ResponseObject.builder()
                .status(200)
                .message("Reset password success")
                .build();
    }
    private boolean isPasswordValid(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$)(?=.*[a-zA-Z0-9]).{8,}$";
        return password.matches(regex);
    }
}
