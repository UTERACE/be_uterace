package com.be_uterace.projection;

public interface UserRankingProjection {
    Long getU_user_id();
    String getU_firstname();
    String getU_lastname();
    String getU_avatar_path();
    String getU_gender();
    Double getU_pace();
    Double getU_total_distance();
    Long getU_ranking();
    Long getU_org_id();
}


