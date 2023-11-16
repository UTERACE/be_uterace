package com.be_uterace.service.impl;

import com.be_uterace.entity.User;
import com.be_uterace.payload.response.ManageUserInitializeResponse;
import com.be_uterace.payload.response.ManageUserStatusResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.UserRepository;
import com.be_uterace.service.ManageUserService;
import com.be_uterace.utils.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManageUserServiceImpl implements ManageUserService {

    private UserRepository userRepository;

    public ManageUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseObject lockUser(Integer user_id) {
        userRepository.markLockUser("0",user_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Khóa tài khoản người dùng thành công");
        return responseObject;
    }

    @Override
    public ResponseObject unlockUser(Integer user_id) {
        userRepository.markLockUser("1",user_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Mở khóa tài khoản người dùng thành công");
        return responseObject;
    }

    @Override
    public ManageUserStatusResponse findAllUserStatus(int current_page, int per_page, String search) {
        Page<User> userPage;
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        if(search==null || search.equals("")) {
            userPage = userRepository.findAll(pageable);
        } else {
            userPage = userRepository.searchUsers(search,pageable);
        }
        List<User> userList = userPage.getContent();
        List<ManageUserStatusResponse.UserStatus> userStatusList = new ArrayList<>();
        for (User user : userList){
            ManageUserStatusResponse.UserStatus userStatus = new ManageUserStatusResponse.UserStatus();
            userStatus.setUser_id(user.getUserId());
            userStatus.setFirst_name(user.getFirstName());
            userStatus.setLast_name(user.getLastName());
            userStatus.setImage(user.getAvatarPath());
            userStatus.setGender(user.getGender());
            userStatus.setPace(user.getPace());
            userStatus.setTotalDistance(user.getTotalDistance());
            userStatus.setOrganization(user.getOrganization());
            userStatus.setStatus(user.getStatus());
            userStatus.setReason_block(user.getReason());
            userStatusList.add(userStatus);
        }
        return ManageUserStatusResponse.builder()
                .per_page(userPage.getSize())
                .total_user((int) userPage.getTotalElements())
                .current_page(userPage.getNumber() + 1)
                .total_page(userPage.getTotalPages())
                .users(userStatusList)
                .build();
    }

    @Override
    public ManageUserInitializeResponse findAllUserInitialize(int current_page, int per_page, String search) {
        Page<User> userPage;
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        if(search==null || search.equals("")) {
            userPage = userRepository.findAll(pageable);
        } else {
            userPage = userRepository.searchUsers(search,pageable);
        }
        List<User> userList = userPage.getContent();
        List<ManageUserInitializeResponse.UserInitialize> userInitializeList = new ArrayList<>();
        for (User user : userList){
            ManageUserInitializeResponse.UserInitialize userInitialize = new ManageUserInitializeResponse.UserInitialize();
            userInitialize.setUser_id(user.getUserId());
            userInitialize.setFirst_name(user.getFirstName());
            userInitialize.setLast_name(user.getLastName());
            userInitialize.setImage(user.getAvatarPath());
            userInitialize.setLast_sync(user.getLast_sync());
            userInitialize.setStatus(user.getSyncStatus());
            userInitializeList.add(userInitialize);
        }
        return ManageUserInitializeResponse.builder()
                .per_page(userPage.getSize())
                .total_user((int) userPage.getTotalElements())
                .current_page(userPage.getNumber() + 1)
                .total_page(userPage.getTotalPages())
                .user_initialize(userInitializeList)
                .build();
    }
}
