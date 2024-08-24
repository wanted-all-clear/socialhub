package com.allclear.socialhub.post.repository;

import com.allclear.socialhub.post.domain.Post;
import com.allclear.socialhub.post.repository.querydsl.PostRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuerydsl {

}
