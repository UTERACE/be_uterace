package com.be_uterace.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClubUpdateDto {
    private Integer club_id;
    private String name;
    private String description;
    private String image;
    //private String details;
    private Double min_pace;
    private Double max_pace;

}
