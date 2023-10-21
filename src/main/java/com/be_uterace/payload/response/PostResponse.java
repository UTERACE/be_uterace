package com.be_uterace.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Integer new_id;
    private String name;
    private String description;
    private String image;
    private Date created_at;
    private Date updated_at;

}
