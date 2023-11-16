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
    public ResponseObject lockPost(Integer post_id) {
        postRepository.markLockPost("0",post_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Khóa bài viết thành công");
        return responseObject;
    }

    @Override
    public ResponseObject unlockPost(Integer post_id) {
        postRepository.markLockPost("1",post_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Mở khóa bài viết thành công");
        return responseObject;
    }

    @Override
    public ResponseObject outstandingPost(Integer post_id) {
        postRepository.markOutstandingPost("1",post_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Chọn bài viết nổi bật thành công");
        return responseObject;
    }

    @Override
    public ResponseObject notOutstandingPost(Integer post_id) {
        postRepository.markOutstandingPost("0",post_id);
        ResponseObject responseObject = new ResponseObject(StatusCode.SUCCESS,"Xóa bài viết nổi bật thành công");
        return responseObject;
    }
}
