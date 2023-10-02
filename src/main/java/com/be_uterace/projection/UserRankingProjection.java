package com.be_uterace.projection;

public interface UserRankingProjection {
    Long getUserId();
    String getFirstName();
    String getLastName();
    String getAvatarPath();
    String getGender();
    Double getPace();
    Double getTotalDistance();
    Long getRanking();
    String getOrganization();
}


