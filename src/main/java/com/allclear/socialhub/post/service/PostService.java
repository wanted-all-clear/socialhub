package com.allclear.socialhub.post.service;

import com.allclear.socialhub.post.dto.PostPaging;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostPaging getPosts(Pageable pageable);

}
