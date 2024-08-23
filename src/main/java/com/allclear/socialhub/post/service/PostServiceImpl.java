package com.allclear.socialhub.post.service;

import com.allclear.socialhub.post.dto.PostPaging;
import com.allclear.socialhub.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    /**
     * 1. 게시물 목록 조회
     * 작성자 : 유리빛나
     *
     * @param pageable Pagination 요청 정보 관련 인터페이스
     * @return 페이징 처리가 된 게시물 전체 목록
     */
    public PostPaging getPosts(Pageable pageable) {

        return new PostPaging(postRepository.getPosts(pageable));
    }


}
