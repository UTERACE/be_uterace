package com.be_uterace.payload.response;

import com.be_uterace.entity.Organization;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class ManageUserInitializeResponse {
    private int per_page;
    private int current_page;
    private int total_page;
    private int total_user;
    private List<ManageUserInitializeResponse.UserInitialize> user_initialize;

    // getters and setters

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor
    public static class UserInitialize {
        private Long user_id;
        private String first_name;
        private String last_name;
        private String image;
        private Date last_sync;
        private String status;

    }
}
