package com.be_uterace.payload.response;

import com.be_uterace.entity.Organization;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class ManageUserStatusResponse {
    private int per_page;
    private int current_page;
    private int total_page;
    private int total_user;
    private List<UserStatus> users;

    // getters and setters

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    @NoArgsConstructor
    public static class UserStatus {
        private Long user_id;
        private String first_name;
        private String last_name;
        private String image;
        private String gender;
        private Double pace;
        private Double totalDistance;
        private String organization;
        private String status;
        private String reason_block;

        public void setOrganization(Organization organization) {
            this.organization = (organization != null) ? organization.getOrgName() : null;
        }
    }
}
