package com.be_uterace.projection;

public interface ClubProjection {
    Integer getClubId();
    String getClubName();
    String getPicturePath();
    Double getClubTotalDistance();
    Long getMemberCount();
}
