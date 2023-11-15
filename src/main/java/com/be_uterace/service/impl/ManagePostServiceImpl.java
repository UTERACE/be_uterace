package com.be_uterace.service.impl;

import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.repository.PostRepository;
import com.be_uterace.service.ManagePostService;
import com.be_uterace.utils.StatusCode;
import org.springframework.stereotype.Service;

@Service
public class ManagePostServiceImpl implements ManagePostService {

    private PostRepository postRepository;

    public ManagePostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public ResponseObject blockPost(Integer post_id) {
        postRepository.markPost("1",post_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Xóa bài viết thành công");
        return responseObject;
    }
}
