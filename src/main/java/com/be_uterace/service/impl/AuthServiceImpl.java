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
                        .id(response.getId())
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
                        .id(response.getId())
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
        userRepository.findByUsername(registerDto.getUsername())
                .ifPresent(user -> {
                    throw new ResourceConflictException("Username");
                });
        userRepository.findByEmail(registerDto.getEmail())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
                });
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=" + recaptchaSecret + "&response=" + registerDto.getRecaptcha_token();
        RestTemplate restTemplate = new RestTemplate();
        RecaptchaResponse response = restTemplate.getForObject(url + params, RecaptchaResponse.class);

        if(response.isSuccess()&&response.getScore()>=0.5) {
            // CAPTCHA xác thực thành công
            registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            User user = convertFromRegisterDtoToUser(registerDto);
            user.setArea(areaRepository.findArea(registerDto.getProvince(),
                    registerDto.getDistrict(),
                    registerDto.getWard()));
            if (registerDto.getType_account().equals("google")) {
                user.setTypeAccount("google");
                user.setPassword(registerDto.getPassword());
                user.setUsername(registerDto.getEmail());
                user.setAvatarPath(registerDto.getImage());
            }
            else if (registerDto.getType_account().equals("facebook")) {
                user.setTypeAccount("facebook");
                user.setUsername(registerDto.getUsername()+"-fb");
                user.setPassword(registerDto.getPassword());
                user.setAvatarPath(registerDto.getImage());
            }
            else if (registerDto.getType_account().equals("default")) {
                user.setTypeAccount("default");
                user.setAvatarPath("");
            }
            user.setHomeNumber(registerDto.getAddress());
            userRepository.save(user);
            return ResponseObject.builder().status(201)
                    .message(Constant.SUCCESS_REGISTER)
                    .build();
        }
        return ResponseObject.builder().status(400)
                .message(Constant.REGISTER_FAIL)
                .build();
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

        int passwordLength = 12;
        String randomPassword = generateRandomPassword(passwordLength);
        String randomPasswordEncoder = passwordEncoder.encode(randomPassword);

        Optional<User> userOptional = userRepository.findByUsername(registerDto.getUsername());
        User user = userOptional.get();
        user.setPassword(randomPasswordEncoder);
        userRepository.save(user);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(registerDto.getEmail())
                .subject("Password Reset Request")
                .msgBody("New password:" + randomPassword)
                .build();
        Boolean status
                = emailService.sendSimpleMail(emailDetails);

        return ResponseObject.builder()
                .status(200)
                .message("Reset mật khẩu thành công")
                .build();
    }
}
