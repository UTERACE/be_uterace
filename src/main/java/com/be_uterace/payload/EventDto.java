package com.be_uterace.payload;

import com.be_uterace.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {
    private Long eventId;
    private String description;
    private String title;
    private Date createAt;
    private Date startDate;
    private Date endDate;
    private String status;
    private Double runningCategory;
    private Integer numOfAttendee;
    private Integer numOfRunner;
    private Double totalDistance;
    private String content;
    private Double maxPace;
    private Integer outstanding;
    private User createUser;
    private Double minPace;
    private User adminUser;
}
