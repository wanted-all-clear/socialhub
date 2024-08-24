package com.allclear.socialhub.post.common.hashtag.service;

import com.allclear.socialhub.post.common.hashtag.domain.Hashtag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HashtagService {

    List<Hashtag> toEachHashTag(List<String> hashtagList);

}
