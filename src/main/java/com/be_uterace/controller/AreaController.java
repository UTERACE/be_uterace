package com.be_uterace.controller;

import com.be_uterace.service.AreaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/area")
public class AreaController {
    private AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping(value = {"/province"})
    public ResponseEntity<List<Map<String, Object>>> getProvinceController() {
        List<Map<String, Object>> areaResponseList = areaService.getAllProvince();
        return ResponseEntity.ok(areaResponseList);
    }

    @GetMapping(value = {"/district"})
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getDistrictController(@RequestParam String province) {
        List<Map<String, Object>> areaResponseList = areaService.getDistrictByProvince(province);
        return ResponseEntity.ok(areaResponseList);
    }

    @GetMapping(value = {"/precinct"})
    public ResponseEntity<List<Map<String, Object>>> getDistrictController(@RequestParam String district, @RequestParam String province) {
        List<Map<String, Object>> areaResponseList = areaService.getPrecinctByDistrictAndProvince(district,province);
        return ResponseEntity.ok(areaResponseList);
    }
}
