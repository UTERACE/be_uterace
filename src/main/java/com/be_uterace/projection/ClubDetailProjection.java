package com.be_uterace.projection;

import java.util.Date;

public interface ClubDetailProjection {
    Integer getClubId();
    String getPicturePath();
    String getClubName();
    String getDescription();
    Integer getTotalMember();
    Double getTotalDistance();
    Integer getTotalActivities();
    Date getCreatedAt();
    String getAdmin();
    Long getAdminId();
    Integer getNumMales();
    Integer getNumFemales();
    Double getMinPace();
    Double getMaxPace();
    String getDetails();
}
