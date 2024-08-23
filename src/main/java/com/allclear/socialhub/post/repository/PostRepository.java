package com.allclear.socialhub.post.repository;

import com.allclear.socialhub.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
