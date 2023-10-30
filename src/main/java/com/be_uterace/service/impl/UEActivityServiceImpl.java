package com.be_uterace.service.impl;

import com.be_uterace.payload.request.DeleteActivityEvent;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.UEActivityRepository;
import com.be_uterace.service.UEActivityService;
import com.be_uterace.utils.StatusCode;
import org.springframework.stereotype.Service;

@Service
public class UEActivityServiceImpl implements UEActivityService {
    private UEActivityRepository ueActivityRepository;

    public UEActivityServiceImpl(UEActivityRepository ueActivityRepository) {
        this.ueActivityRepository = ueActivityRepository;
    }

    @Override
    public ResponseObject deleteActivity(DeleteActivityEvent req) {
        ueActivityRepository.updateStatusAndReasonByEventIdAndRunId(
                req.getReason(),req.getEvent_id(),req.getActivity_id()
        );
        return new ResponseObject(StatusCode.SUCCESS,"Xóa hoạt động thành công");
    }
}
