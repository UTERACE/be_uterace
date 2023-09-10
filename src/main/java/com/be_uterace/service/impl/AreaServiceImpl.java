package com.be_uterace.service.impl;

import com.be_uterace.entity.Area;
import com.be_uterace.payload.AreaDto;
import com.be_uterace.payload.response.AreaResponse;
import com.be_uterace.repository.AreaRepository;
import com.be_uterace.service.AreaService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class AreaServiceImpl implements AreaService {

    private AreaRepository areaRepository;

    public AreaServiceImpl(AreaRepository areaRepository, ModelMapper modelMapper) {
        this.areaRepository = areaRepository;
    }

    @Override
    public List<Map<String, Object>> getAllProvince() {
        // Lấy Area entity ra từ DB
        List<Area> areaList = areaRepository.findByDistrictAndPrecinctAndStatus("", "", "1");

        // Sử dụng Java Streams để ánh xạ thành danh sách các bản ghi JSON tùy chỉnh
        List<Map<String, Object>> result = areaList.stream()
                .map(area -> {
                    Map<String, Object> areaJson = new HashMap<>();
                    areaJson.put("province_id", area.getProvince());
                    areaJson.put("province_name", area.getName());
                    return areaJson;
                })
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<Map<String, Object>> getDistrictByProvince(String province_id) {
        // Lấy Area entity ra từ DB
        List<Area> areaList = areaRepository.
                findDistrictByProvince(province_id);

        // Sử dụng Java Streams để ánh xạ thành danh sách các bản ghi JSON tùy chỉnh
        List<Map<String, Object>> result = new ArrayList<>();
        for (Area area : areaList) {
            Map<String, Object> areaJson = new LinkedHashMap<>();
            areaJson.put("district_id", area.getDistrict());
            areaJson.put("district_name", area.getName());
            result.add(areaJson);
        }


        return result;
    }

    @Override
    public List<Map<String, Object>> getPrecinctByDistrictAndProvince(String district_id, String province_id) {
        // Lấy Area entity ra từ DB
        List<Area> areaList = areaRepository.
                findPrecinctByDistrictAndProvince(district_id, province_id);

        // Sử dụng Java Streams để ánh xạ thành danh sách các bản ghi JSON tùy chỉnh
        List<Map<String, Object>> result = areaList.stream()
                .map(area -> {
                    Map<String, Object> areaJson = new HashMap<>();
                    areaJson.put("precinct_id", area.getPrecinct());
                    areaJson.put("precinct_name", area.getName());
                    return areaJson;
                })
                .collect(Collectors.toList());

        return result;
    }

}
