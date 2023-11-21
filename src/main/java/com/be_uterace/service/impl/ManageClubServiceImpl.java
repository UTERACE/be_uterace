package com.be_uterace.service.impl;

import com.be_uterace.entity.Club;
import com.be_uterace.entity.User;
import com.be_uterace.payload.response.ManageClubSearchResponse;
import com.be_uterace.payload.response.ManageUserStatusResponse;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.ClubRepository;
import com.be_uterace.service.ManageClubService;
import com.be_uterace.utils.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManageClubServiceImpl implements ManageClubService {

    private ClubRepository clubRepository;

    public ManageClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public ResponseObject lockClub(Integer club_id) {
        clubRepository.markLockClub("0",club_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Khóa clb thành công");
        return responseObject;
    }

    @Override
    public ResponseObject unlockClub(Integer club_id) {
        clubRepository.markLockClub("1",club_id);
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

    @Override
    public ManageClubSearchResponse searchCLub(int current_page, int per_page, String search) {
        Page<Club> clubPage;
        Pageable pageable = PageRequest.of(current_page - 1, per_page);
        if(search==null || search.equals("")) {
            clubPage = clubRepository.findAll(pageable);
        } else {
            clubPage = clubRepository.searchClubManage(search,pageable);
        }
        List<Club> clubList = clubPage.getContent();
        List<ManageClubSearchResponse.Club> clubListResponse = new ArrayList<>();
        for (Club club : clubList){
            ManageClubSearchResponse.Club clubResponse = new ManageClubSearchResponse.Club();
            clubResponse.setClub_id(club.getClubId());
            clubResponse.setName(club.getClubName());
            clubResponse.setImage(club.getPicturePath());
            clubResponse.setTotal_members((club.getNumOfMales() != null ? club.getNumOfMales() : 0) + (club.getNumOfFemales() != null ? club.getNumOfFemales() : 0));
            clubResponse.setTotal_distance(club.getClubTotalDistance());
            clubResponse.setOutstanding(club.getOutstanding());
            clubResponse.setStatus(club.getStatus());
            clubResponse.setReason_block(club.getReason());
            clubListResponse.add(clubResponse);
        }
        return ManageClubSearchResponse.builder()
                .per_page(clubPage.getSize())
                .total_clubs((int) clubPage.getTotalElements())
                .current_page(clubPage.getNumber() + 1)
                .total_page(clubPage.getTotalPages())
                .clubs(clubListResponse)
                .build();
    }
}
