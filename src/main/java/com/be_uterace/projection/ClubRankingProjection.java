package com.be_uterace.projection;

public interface ClubRankingProjection {
    Long getClubId();
    Integer getClubRanking();
    String getClubName();
    String getPicturePath();
    Double getClubTotalDistance();
    Long getMemberCount();
    Long getEventCount();
}
