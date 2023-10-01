package com.be_uterace.projection;

public interface ScoreboardClubProjection {
    Long getClubId();
    String getClubName();
    String getPicturePath();
    Double getTotalDistance();
    Long getTotalMembers();
    Long getTotalActivities();
    Integer getRanking();
}

