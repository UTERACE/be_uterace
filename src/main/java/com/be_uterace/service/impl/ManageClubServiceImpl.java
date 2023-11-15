package com.be_uterace.service.impl;

import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.service.ManageClubService;
import com.be_uterace.utils.StatusCode;
import org.springframework.stereotype.Service;

@Service
public class ManageClubServiceImpl implements ManageClubService {

    private ClubRepository clubRepository;

    public ManageClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public ResponseObject lockClub(Integer club_id) {
        clubRepository.markLockClub("1",club_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Khóa clb thành công");
        return responseObject;
    }

    @Override
    public ResponseObject unlockClub(Integer club_id) {
        clubRepository.markLockClub("0",club_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Mở khóa clb thành công");
        return responseObject;
    }

    @Override
    public ResponseObject outstandingClub(Integer club_id) {
        clubRepository.markOutstandingClub("1",club_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Chọn clb nổi bật thành công");
        return responseObject;
    }

    @Override
    public ResponseObject notOutstandingClub(Integer club_id) {
        clubRepository.markOutstandingClub("0",club_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Xóa clb nổi bật thành công");
        return responseObject;
    }
}
