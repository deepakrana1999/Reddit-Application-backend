package com.programming.ranatech.springredditclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programming.ranatech.springredditclone.model.Comment;
import com.programming.ranatech.springredditclone.model.Post;
import com.programming.ranatech.springredditclone.model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
     List<Comment> findByPost(Post post);
     
     List<Comment> findByUser(User user);
     
}
