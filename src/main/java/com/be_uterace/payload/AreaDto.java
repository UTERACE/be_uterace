package com.be_uterace.payload;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaDto implements Serializable {
    private Long areaId;
    private String province;
    private String district;
    private String precinct;
    private String name;
    private String fullName;
    private String status;
}
