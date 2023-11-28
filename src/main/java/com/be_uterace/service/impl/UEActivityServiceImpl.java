package com.be_uterace.service.impl;

import com.be_uterace.payload.request.DeleteActivityEvent;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.UEActivityRepository;
import com.be_uterace.service.UEActivityService;
import com.be_uterace.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UEActivityServiceImpl implements UEActivityService {
    private UEActivityRepository ueActivityRepository;

    public UEActivityServiceImpl(UEActivityRepository ueActivityRepository) {
        this.ueActivityRepository = ueActivityRepository;
    }

    @Override
    public ResponseObject deleteActivity(DeleteActivityEvent req) {
        int updatedRows = ueActivityRepository.updateStatusAndReasonByEventIdAndRunId(
                req.getReason(),req.getEvent_id(),req.getActivity_id()
        );
        return Optional.of(updatedRows)
                .filter(rows -> rows > 0)
                .map(rows -> new ResponseObject(StatusCode.SUCCESS, "Xóa hoạt động thành công"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event hoặc Activity không tồn tại"));
    }
}
