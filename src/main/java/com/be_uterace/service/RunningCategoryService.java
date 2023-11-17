package com.be_uterace.service;

import com.be_uterace.entity.RunningCategory;
import com.be_uterace.payload.request.RunningCategoryDto;
import com.be_uterace.payload.response.ResponseObject;

import java.util.List;

public interface RunningCategoryService {
    List<RunningCategory> getAllRunningCategory();
    RunningCategory getRunningCategoryById(Integer running_category_id);
    ResponseObject addRunningCategory(RunningCategoryDto runningCategoryDto);
    ResponseObject updateRunningCategory(Integer running_category_id,RunningCategoryDto runningCategoryDto);
    ResponseObject deleteRunningCategory(Integer running_category_id);
}
