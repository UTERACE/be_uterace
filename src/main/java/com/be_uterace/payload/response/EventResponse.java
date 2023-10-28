package com.be_uterace.payload.response;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Integer event_id;
    private String name;
    private String image;
    private Integer total_members;
    private Integer total_clubs;

}
