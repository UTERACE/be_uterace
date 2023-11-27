package com.be_uterace.projection;

public interface ClubRankingProjection {
    Integer getC_club_id();
    String getC_club_name();
    String getC_picture_path();
    Double getC_total_distance();
    Integer getC_num_of_attendee();
    Long getC_total_activities();
    Integer getC_ranking();
}
