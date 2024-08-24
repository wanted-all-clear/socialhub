package com.allclear.socialhub.post.common.hashtag.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.allclear.socialhub.post.common.hashtag.domain.Hashtag;

@Service
public interface HashtagService {

	List<Hashtag> toEachHashTag(List<String> hashtagList);

}
