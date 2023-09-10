package com.be_uterace.service;

import com.be_uterace.payload.AreaDto;
import com.be_uterace.payload.response.AreaResponse;
import com.be_uterace.payload.response.ResponseObject;

import java.util.List;
import java.util.Map;

public interface AreaService {
    List<Map<String, Object>> getAllProvince();

    List<Map<String, Object>> getDistrictByProvince(String province_id);

    List<Map<String, Object>> getPrecinctByDistrictAndProvince(String district_id, String province_id);

}
