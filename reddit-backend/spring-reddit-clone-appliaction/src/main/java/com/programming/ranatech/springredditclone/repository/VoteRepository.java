package com.programming.ranatech.springredditclone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programming.ranatech.springredditclone.model.Post;
import com.programming.ranatech.springredditclone.model.User;
import com.programming.ranatech.springredditclone.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser );
}
