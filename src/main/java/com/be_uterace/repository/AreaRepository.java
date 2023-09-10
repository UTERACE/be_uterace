package com.be_uterace.repository;

import com.be_uterace.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area,Long> {
    //find Province
    List<Area> findByDistrictAndPrecinctAndStatus(String district, String precinct, String status);
    //find District by Province
    @Query("SELECT a FROM Area a WHERE a.district <> '' AND a.precinct = '' AND a.province = :province AND a.status ='1'")
    List<Area> findDistrictByProvince(@Param("province") String province);

    @Query("SELECT a FROM Area a WHERE a.precinct <> '' AND a.district = :district AND a.province = :province AND a.status ='1'")
    List<Area> findPrecinctByDistrictAndProvince(@Param("district") String district, @Param("province") String province);

    @Query("SELECT a FROM Area a WHERE a.precinct = :precinct AND a.district = :district AND a.province = :province AND a.status ='1'")
    Area findArea(String province, String district, String precinct);

}
