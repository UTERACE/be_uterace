package com.be_uterace.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RunningCategoryResponse {
    private Integer id;
    private String name;
    private Double distance;
}
