package com.allclear.socialhub.post.common.share.repository;

import com.allclear.socialhub.post.common.share.domain.PostShare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<PostShare, Long> {
}
