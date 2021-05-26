package com.decagon.obamax.BlogRest.repository;

import com.decagon.obamax.BlogRest.model.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
    List<PostLikes> findByUserIdAndPostId(Long userId, Long postId);
}
