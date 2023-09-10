package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AreaResponse {
    @JsonAlias({"provinceId", "DistrictId", "PrecinctId"})
    private Long id;

    @JsonAlias({"provinceName", "DistrictName", "PrecinctName"})
    private String name;
}
