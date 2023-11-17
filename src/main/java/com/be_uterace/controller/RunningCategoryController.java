package com.be_uterace.controller;

import com.be_uterace.entity.RunningCategory;
import com.be_uterace.payload.request.RunningCategoryDto;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.service.RunningCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/distance")
public class RunningCategoryController {
    private RunningCategoryService runningCategoryService;
    public RunningCategoryController(RunningCategoryService runningCategoryService) {
        this.runningCategoryService = runningCategoryService;
    }

    @GetMapping()
    public ResponseEntity<List<RunningCategory>> getAllRunningCategoryController(){
        List<RunningCategory> runningCategories = runningCategoryService.getAllRunningCategory();
        return ResponseEntity.status(HttpStatus.OK).body(runningCategories);
    }

    @GetMapping("/{running_category_id}")
    public ResponseEntity<RunningCategory> getRunningCategoryByIdController(@PathVariable Integer running_category_id){
        RunningCategory runningCategory = runningCategoryService.getRunningCategoryById(running_category_id);
        return ResponseEntity.status(HttpStatus.OK).body(runningCategory);
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> addRunningCategoryController(@RequestBody RunningCategoryDto runningCategoryDto) {
        ResponseObject responseObject = runningCategoryService.addRunningCategory(runningCategoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @PutMapping("/{running_category_id}")
    public ResponseEntity<ResponseObject> updateRunningCategoryController(@PathVariable Integer running_category_id,
                                                                          @RequestBody RunningCategoryDto runningCategoryDto) {
        ResponseObject responseObject = runningCategoryService.updateRunningCategory(running_category_id, runningCategoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @DeleteMapping("/{running_category_id}")
    public ResponseEntity<ResponseObject> deleteRunningCategoryController(@PathVariable Integer running_category_id) {
        ResponseObject responseObject = runningCategoryService.deleteRunningCategory(running_category_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }
}
