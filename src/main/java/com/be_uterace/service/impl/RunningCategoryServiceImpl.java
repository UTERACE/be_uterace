package com.be_uterace.service.impl;

import com.be_uterace.entity.RunningCategory;
import com.be_uterace.payload.request.RunningCategoryDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.RunningCategoryRepository;
import com.be_uterace.service.RunningCategoryService;
import com.be_uterace.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RunningCategoryServiceImpl implements RunningCategoryService {
    @Autowired
    private RunningCategoryRepository runningCategoryRepository;
    @Override
    public List<RunningCategory> getAllRunningCategory() {
        return runningCategoryRepository.findAll();
    }

    @Override
    public ResponseObject addRunningCategory(RunningCategoryDto runningCategoryDto) {
        RunningCategory runningCategory = new RunningCategory();
        runningCategory.setRunningCategoryName(runningCategoryDto.getRunningCategoryName());
        runningCategory.setRunningCategoryDistance(runningCategoryDto.getRunningCategoryDistance());
        runningCategoryRepository.save(runningCategory);
        return new ResponseObject(StatusCode.SUCCESS,"Add running category successfully");
    }

    @Override
    public ResponseObject updateRunningCategory(Integer running_category_id,RunningCategoryDto runningCategoryDto) {
        RunningCategory runningCategory = runningCategoryRepository.findById(running_category_id).orElse(null);
        if(runningCategory == null){
            return new ResponseObject(StatusCode.NOT_FOUND,"Running category not found");
        }
        runningCategory.setRunningCategoryName(runningCategoryDto.getRunningCategoryName());
        runningCategory.setRunningCategoryDistance(runningCategoryDto.getRunningCategoryDistance());
        runningCategoryRepository.save(runningCategory);
        return new ResponseObject(StatusCode.SUCCESS,"Update running category successfully");
    }

    @Override
    public ResponseObject deleteRunningCategory(Integer running_category_id) {
        RunningCategory runningCategory = runningCategoryRepository.findById(running_category_id).orElse(null);
        if(runningCategory == null){
            return new ResponseObject(StatusCode.NOT_FOUND,"Running category not found");
        }
        runningCategoryRepository.delete(runningCategory);
        return new ResponseObject(StatusCode.SUCCESS,"Delete running category successfully");
    }
}
